---
icon: material/bug-check
---

# Debugging Quest Logic

Broken quest logic is easier to fix when you test one layer at a time. Most issues come from one of five places:
the quest was not reloaded, a condition has a different result than expected, an objective was never added or is blocked,
an action does not run the effect you expect, or a placeholder resolves to an empty or unexpected value.

The commands below are meant to be applied to any quest package. Replace `Steve`, `my_package`, and IDs such as
`someCondition`, `someObjective`, and `someAction` with the player, package, and entry you are testing.

The goal is:

- Reload the quest package and read the error output first.
- Check whether a condition is true for the player.
- Check whether the objective is active.
- Run actions directly and split container actions into smaller checks.
- Print placeholder values where the player can see them.
- Reset tags, points, and objectives while testing.
- Use ingame debug output when the reason is not obvious.

## Step 1: reload and check for config errors

Save your files and reload BetonQuest before testing:

```text
/bq reload
```

If the reload prints warnings or errors, fix those first. Actions, conditions, objectives, and configuration values with
invalid placeholders cannot be tested correctly with commands.

When the reload output is too noisy, enable ingame debug output for your package:

```text
/bq debug ingame my_package info
```

Use `debug` instead of `info` only when you need more detail, because debug output can be very spammy.
Run `/bq debug ingame` without arguments to see your active filters.

## Step 2: check conditions directly

Conditions can be tested without starting the whole quest.
Use the player name and the full condition ID:

```text
/bq condition Steve my_package>someCondition
```

Expected result before the related quest state is created:

```text
false
```

Run the action that should make the condition true:

```text
/bq action Steve my_package>someAction
```

Then check the condition again:

```text
/bq condition Steve my_package>someCondition
```

Expected result after the related quest state is created:

```text
true
```

If the condition is still false, inspect the player data that the condition depends on. For tag-based conditions, list
the player's tags:

```text
/bq tag Steve list
```

If the expected tag is missing, test the action that should add it directly:

```text
/bq action Steve my_package>addExpectedTag
```

Then run `/bq tag Steve list` again. The same pattern applies to point-, objective-, item-, and location-based
conditions: inspect the data source, run the action that should change it, and test the condition again.

## Step 3: check whether the objective is active

Objectives only track progress after they were added to the player.
List the player's active objectives:

```text
/bq objective Steve list
```

If the expected objective is not listed, add it manually:

```text
/bq objective Steve add my_package>someObjective
```

Then perform one action that should progress the objective, such as clicking the NPC, breaking the block, killing the
entity, entering the location, or using the item required by the objective.
If nothing happens, the objective is probably blocked by its condition, uses the wrong block/item type, or was configured
with a location/range that the player is not matching.

You can also complete the objective manually to check whether the completion actions work:

```text
/bq objective Steve complete my_package>someObjective
```

This should run the objective's completion actions.
If the reward or completion message does not appear, debug the actions attached to the objective before changing the
objective itself.

## Step 4: debug actions directly

Actions are often the easiest part to isolate because they can be run without triggering the conversation, objective, or
schedule that normally calls them. Start with the action that should be reached by the quest flow:

```text
/bq action Steve my_package>someAction
```

If this works, the action itself is valid and the problem is probably in the trigger that should call it. If it does not
work, split container actions and test each child action:

```text
/bq action Steve my_package>firstChildAction
/bq action Steve my_package>secondChildAction
/bq action Steve my_package>thirdChildAction
```

Folder, `if`, `first`, `pickrandom`, and delayed actions are useful in quests, but during debugging they can hide which
child action failed or which branch was selected. Test the child action first, then test the container action again.

When an action changes player data, verify the data after running it:

```text
/bq tag Steve list
/bq point Steve list
/bq objective Steve list
```

If an action is player-independent, replace the player name with `-`:

```text
/bq action - my_package>resetGlobalState
```

Only actions that support the independent context can be tested this way. If an action requires an online player, run it
with a player name and check the console output if it fails.

## Step 5: inspect placeholders

Placeholders are resolved at the point where text is rendered. A placeholder that looks correct in configuration can
still produce an empty value if the referenced player data does not exist, the objective is not active, or the wrong
package scope is used.

The simplest way to inspect a placeholder is to print it through a temporary `notify` action:

```YAML title="Temporary placeholder checks"
actions:
  debugObjective: "notify &7objective left: %objective.someObjective.left%"
  debugCondition: "notify &7condition: %condition.someCondition%"
  debugPoints: "notify &7points: %point.someCategory.amount%"
```

Run the debug action manually:

```text
/bq action Steve my_package>debugObjective
```

If the output is empty, check the data source behind the placeholder. For objective placeholders, list active objectives.
For point placeholders, list points. For condition placeholders, test the condition directly.

```text
/bq objective Steve list
/bq point Steve list
/bq condition Steve my_package>someCondition
```

When using placeholders inside actions that contain spaces, commas, or nested placeholders, quote the action value in
YAML and check whether the action type has special placeholder syntax. If the placeholder works in a simple `notify`
action but fails in a more complex action, the problem is usually quoting, escaping, or argument parsing rather than the
placeholder itself.

## Step 6: reset test data

Testing often leaves old tags, points, or objectives on the player.
Reset only the data that belongs to the quest you are testing:

```text
/bq objective Steve del my_package>someObjective
/bq tag Steve del my_package>some_started_tag
/bq tag Steve del my_package>some_done_tag
```

If the quest uses points, list and remove them too:

```text
/bq point Steve list
/bq point Steve del my_package>someCategory
```

If the quest added journal entries, variables, or other persistent data, reset those as well before retesting the full
flow from the player's entry point.

After resetting, start the quest again from the same entry point players use.
This catches problems that manual commands can hide.

## Common causes

- The file was saved, but `/bq reload` was not run.
- A command uses `conditionName`, `actionName`, or `objectiveName`, but the real ID is package-scoped.
- The objective was defined correctly, but never added to the player.
- The objective is active, but one of its `conditions:` is false.
- An action works when run directly, but the conversation, objective, schedule, or menu never calls it.
- A folder or conditional action hides which child action or branch failed.
- An action has the wrong execution context; for example, it requires an online player but is run independently.
- A placeholder is empty because the referenced objective, point, variable, or condition does not exist for that player.
- A placeholder works in `notify`, but fails inside another action because of quoting or escaping.
- A tag or point from an old test still changes the quest path.
- The objective listens for the wrong material, entity, location, or amount.
- A conversation option is hidden because its conditions are checked before the player reaches that option.

When debugging, work from the outside in:
reload first, test the condition, list the objective, trigger the action, inspect placeholders, and only then test the
full player flow.


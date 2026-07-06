---
icon: material/bug-check
---

# How to debug broken conditions and objectives

Broken conditions and objectives are easier to fix when you test one part at a time.
Most issues come from one of three places: the quest was not reloaded, the player has the wrong quest data,
or the objective is active but blocked by a condition.

The following example shows a small quest where the player should mine 5 coal after accepting the quest.
It also shows the commands you can use to inspect each step.

The goal is:

- Reload the quest package and read the error output first.
- Check whether a condition is true for the player.
- Check whether the objective is active.
- Test actions manually before testing the full quest flow.
- Reset tags, points, and objectives while testing.
- Use ingame debug output when the reason is not obvious.

## Example quest

This quest starts a mining objective only after the player accepts it.
The `conditions:coalQuestStarted` argument on the objective makes sure coal only counts while the quest is active.

```yaml
objectives:
  mineCoal: "block coal_ore -5 notify conditions:coalQuestStarted actions:finishCoalQuest" #(1)!

actions:
  startCoalQuest: "folder addCoalStarted,addCoalObjective,sendCoalHint" #(2)!
  addCoalStarted: "tag add coal_quest_started" #(3)!
  addCoalObjective: "objective add mineCoal" #(4)!
  sendCoalHint: "notify &6Mine 5 coal ore in the cave."

  finishCoalQuest: "folder addCoalDone,removeCoalObjective,rewardPlayer" #(5)!
  addCoalDone: "tag add coal_quest_done"
  removeCoalObjective: "objective remove mineCoal"
  rewardPlayer: "give reward:3"

conditions:
  coalQuestStarted: "tag coal_quest_started" #(6)!
  coalQuestDone: "tag coal_quest_done"

items:
  reward: "simple EMERALD"
```

1. The objective only progresses while `coalQuestStarted` is true.
2. Starting the quest should create the quest state, add the objective, and notify the player.
3. Adds the tag that the objective condition checks.
4. Starts the objective for the player.
5. Runs after the player mined enough coal.
6. This condition is true when the player has the `coal_quest_started` tag.

## Step 1: reload and check for config errors

Save your files and reload BetonQuest before testing:

```text
/bq reload
```

If the reload prints warnings or errors, fix those first.
An objective or condition that failed to load cannot be tested correctly with commands.

When the reload output is too noisy, enable ingame debug output for your package:

```text
/bq debug ingame my_package info
```

Use `debug` instead of `info` only when you need more detail, because debug output can be very spammy.
Run `/bq debug ingame` without arguments to see your active filters.

## Step 2: check the condition directly

Conditions can be tested without starting the whole quest.
Use the player name and the full condition ID:

```text
/bq condition Steve my_package>coalQuestStarted
```

Expected result before the quest starts:

```text
false
```

Start the quest action manually:

```text
/bq action Steve my_package>startCoalQuest
```

Then check the condition again:

```text
/bq condition Steve my_package>coalQuestStarted
```

Expected result after the quest starts:

```text
true
```

If the condition is still false, inspect the player's tags:

```text
/bq tag Steve list
```

If the tag is missing, test the tag action directly:

```text
/bq action Steve my_package>addCoalStarted
```

Then run `/bq tag Steve list` again.
This tells you whether the problem is the tag action itself or the action that should call it.

## Step 3: check whether the objective is active

Objectives only track progress after they were added to the player.
List the player's active objectives:

```text
/bq objective Steve list
```

If `my_package>mineCoal` is not listed, add it manually:

```text
/bq objective Steve add my_package>mineCoal
```

Then mine one matching block and check whether the progress notification appears.
If nothing happens, the objective is probably blocked by its condition, uses the wrong block/item type, or was configured
with a location/range that the player is not matching.

You can also complete the objective manually to check whether the completion actions work:

```text
/bq objective Steve complete my_package>mineCoal
```

This should run `finishCoalQuest`.
If the reward or completion message does not appear, debug the actions attached to the objective before changing the
objective itself.

## Step 4: isolate actions from objectives

When an objective completes but the quest does not continue, test the completion action directly:

```text
/bq action Steve my_package>finishCoalQuest
```

If this works, the action is valid and the problem is probably in the objective trigger.
If this does not work, split the folder action and test each child action:

```text
/bq action Steve my_package>addCoalDone
/bq action Steve my_package>removeCoalObjective
/bq action Steve my_package>rewardPlayer
```

Folder actions are useful in quests, but during debugging they can hide which child action failed.
Testing each child action makes the failing part visible.

## Step 5: reset test data

Testing often leaves old tags, points, or objectives on the player.
Reset only the data that belongs to the quest you are testing:

```text
/bq objective Steve del my_package>mineCoal
/bq tag Steve del my_package>coal_quest_started
/bq tag Steve del my_package>coal_quest_done
```

If the quest uses points, list and remove them too:

```text
/bq point Steve list
/bq point Steve del my_package>coalProgress
```

After resetting, start the quest again from the same entry point players use.
This catches problems that manual commands can hide.

## Common causes

- The file was saved, but `/bq reload` was not run.
- The command uses `conditionName`, but the real ID is `my_package>conditionName`.
- The objective was defined correctly, but never added to the player.
- The objective is active, but one of its `conditions:` is false.
- A tag or point from an old test still changes the quest path.
- The objective listens for the wrong material, entity, location, or amount.
- A folder action calls multiple actions, but only one child action is broken.
- A conversation option is hidden because its conditions are checked before the player reaches that option.

When debugging, work from the outside in:
reload first, test the condition, list the objective, trigger the action, and only then test the full player flow.


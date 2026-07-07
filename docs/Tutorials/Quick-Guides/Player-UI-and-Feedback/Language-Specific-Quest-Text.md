---
icon: material/translate
---

# How to make language-specific quest text

Language-specific quest text lets players see conversations, notifications, journal entries, menus, and other quest UI
in their selected language.
BetonQuest uses language keys like `en-US` and `de-DE` and falls back to the default language if a translation is
missing.

The following example translates a small delivery quest into English and German.

The goal is:

- Translate conversation text and player answers.
- Translate notify messages.
- Translate journal entries and journal main page text.
- Translate menu item text.
- Reuse translated constants with placeholders.
- Let players switch their quest language with `/questlang`.

```yaml
constants:
  village_name: #(1)!
    en-US: "Stone Village"
    de-DE: "Steindorf"

npcs:
  courierNpc: "citizens 4" #(2)!

npc_conversations:
  courierNpc: "courier" #(3)!

conversations:
  courier:
    quester: #(4)!
      en-US: "Courier"
      de-DE: "Kurier"
    first: "startQuest,alreadyStarted,questDone"

    NPC_options:
      startQuest:
        text: #(5)!
          en-US: "Can you bring this letter to %translate.village_name%?"
          de-DE: "Kannst du diesen Brief nach %translate.village_name% bringen?"
        pointers: "acceptQuest"
        conditions: "!deliveryStarted,!deliveryDone"
      alreadyStarted:
        text:
          en-US: "Please deliver the letter to %translate.village_name%."
          de-DE: "Bitte bring den Brief nach %translate.village_name%."
        conditions: "deliveryStarted,!deliveryDone"
      questDone:
        text:
          en-US: "Thank you for delivering the letter."
          de-DE: "Danke, dass du den Brief geliefert hast."
        conditions: "deliveryDone"

    player_options:
      acceptQuest:
        text:
          en-US: "I will deliver it."
          de-DE: "Ich bringe ihn hin."
        actions: "startDelivery"

journal:
  delivery_started: #(6)!
    en-US: "&0Deliver the courier's letter to %translate.village_name%."
    de-DE: "&0Bring den Brief des Kuriers nach %translate.village_name%."
  delivery_done:
    en-US: "&0The letter was delivered."
    de-DE: "&0Der Brief wurde geliefert."

journal_main_page:
  delivery_status:
    priority: 1
    text: #(7)!
      en-US: "&6Active quest:&0 Deliver the letter."
      de-DE: "&6Aktive Quest:&0 Liefere den Brief."
    conditions: "deliveryStarted,!deliveryDone"

menu_items:
  deliveryActive:
    item: "letterIcon"
    text: #(8)!
      en-US:
        - "&6[Quest] &fCourier Delivery"
        - "&7Deliver the letter to %translate.village_name%."
      de-DE:
        - "&6[Quest] &fKurierlieferung"
        - "&7Bring den Brief nach %translate.village_name%."
    click: "deliveryReminder"
    close: false

objectives:
  reachVillage: "location 120;64;-240;world 5 actions:finishDelivery"

actions:
  startDelivery: "folder addDeliveryStarted,addDeliveryJournal,addDeliveryObjective,deliveryReminder"
  addDeliveryStarted: "tag add delivery_started"
  addDeliveryJournal: "journal add delivery_started"
  addDeliveryObjective: "objective add reachVillage"
  deliveryReminder: "notify {en-US} Deliver the letter to %translate.village_name%. {de-DE} Bring den Brief nach %translate.village_name%. io:actionbar" #(9)!

  finishDelivery: "folder addDeliveryDone,replaceDeliveryJournal,deliveryFinished"
  addDeliveryDone: "tag add delivery_done"
  replaceDeliveryJournal: "folder removeStartedJournal,addDoneJournal"
  removeStartedJournal: "journal delete delivery_started"
  addDoneJournal: "journal add delivery_done"
  deliveryFinished: "notify {en-US} Letter delivered! {de-DE} Brief geliefert! io:title" #(10)!

conditions:
  deliveryStarted: "tag delivery_started"
  deliveryDone: "tag delivery_done"

items:
  letterIcon: "simple PAPER title:&6Letter"
```

1. Constants can be translated and reused with `%translate.village_name%`.
2. Defines the NPC. Replace `citizens 4` with the selector and ID of your own NPC.
3. Binds the `courier` conversation to the NPC so players can start it by interacting with the NPC.
4. The NPC name can be translated in the `quester` field.
5. Conversation option text can use one text per language key.
6. Journal entries use the same language-key structure as conversations.
7. Journal main page text can also be translated.
8. Menu item text can be translated as a list of lore lines.
9. `notify` uses inline language markers like `{en-US}` and `{de-DE}` inside the action string.
10. The next language marker ends the previous translated notify message.

## Switching language

Players can change their quest language with:

```text
/questlang de-DE
```

They can switch back to the configured default language with:

```text
/questlang default
```

Server owners can control selectable languages in the BetonQuest config:

```yaml
language:
  default: en-US
  questlang_whitelist:
    - en-US
    - de-DE
```

If `questlang_whitelist` is empty, every loaded language can be selected.
If it is not empty, only the listed languages are available through `/questlang`.

## Fallback behavior

Always provide the default language for every translated text.
If a player uses a language that is missing on a specific text, BetonQuest uses the default language as fallback.

This is valid:

```yaml
journal:
  delivery_started:
    en-US: "&0Deliver the letter."
    de-DE: "&0Liefere den Brief."
```

This is risky if `en-US` is your default language:

```yaml
journal:
  delivery_started:
    de-DE: "&0Liefere den Brief."
```

## Common patterns

Use translated constants for repeated names:

```yaml
constants:
  miner_name:
    en-US: "Old Miner"
    de-DE: "Alter Bergarbeiter"

actions:
  greetMiner: "notify {en-US} Speak with %translate.miner_name%. {de-DE} Sprich mit %translate.miner_name%."
```

Use the `language` action when a quest should set a player's language directly:

```yaml
actions:
  setEnglish: "language en-US"
  setGerman: "language de-DE"
```

Use the same language keys consistently across the package.
For example, prefer `de-DE` everywhere instead of mixing `de-DE`, `de`, and `german`.

## Troubleshooting

- If text stays in the default language, check the player's `/questlang` setting.
- If a translation is ignored, check that the language key matches a loaded language such as `en-US` or `de-DE`.
- If `/questlang de-DE` is rejected, check `language.questlang_whitelist`.
- If a `%translate...%` placeholder is empty, check that the constant exists and includes the default language.
- If a translated `notify` fails, make sure every language marker has a space after it, for example `{de-DE} Text`.

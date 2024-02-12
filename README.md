**TitanPocketKeyboard** is an Android input method that entirely relies on the embedded keyboard of the Unihertz Titan Pocket and provides most features expected on a full-sized keyboard.

In its current version, it does not have any user interface or settings, and it is optimized for the French language in QWERTY (following the labels), but it should be useful too for the English, Spanish, Portuguese, Italian, German and Dutch languages.

# Installation

1. Download the latest APK from the [releases](https://github.com/oin/TitanPocketKeyboard/releases) page.
2. Install the APK on your Unihertz Titan Pocket.
3. Go to `Settings` > `System` > `Languages & input` > `Virtual keyboard` > `Manage keyboards` and enable `TitanPocketKeyboard`.

# Features

- Full keyboard layout with all keys and symbols.
- No space taken on the screen.
- Long press for alternate characters (see list below).
- Multiple key presses (in quick succession) for accented characters, and other alternate symbols (see list below).
- Keyboard navigation, simulating arrow keys and home/end/page up/page down keys, using the `sym` modifier.
- Lock modifier keys by double-tapping them, while a single tap will only have effect for the next key press.
- See modifier key state in the status bar.
- Auto-capitalization of the first letter of a sentence.
- Two spaces after a period automatically replaced by a period and a space.

# Keyboard layout

The keyboard layout is based on the QWERTY layout of the Unihertz Titan Pocket, extended to allow access to most characters and symbols expected on a full-sized keyboard.

## Additional characters

The following table shows the characters that can be accessed by multiple key presses in quick succession (_multipress_), as well as the characters accessible using a long press (and subsequent multipresses).
The characters in bold are the ones that are marked on the labels of the keys.

| Key | Multipress | Long press | `sym` modifier |
| --- | --- | --- | --- |
| **`q`** |  | **`0`**, `°` (degree) | _Tab_ |
| **`w`** |  | **`1`**, `&`, `↑` | _Up arrow_ |
| **`e`** | `é`, `è`, `ê`, `ë` | **`2`**, `€`, `∃` | `€` |
| **`r`** |  | **`3`**, `®` | |
| **`t`** |  | **`(`**, `[`, `{`, `<`, `≤`, `†`, `™` | |
| **`y`** |  | **`)`**, `]`, `}`, `>`, `≥` | _Home_ |
| **`u`** | `ù`, `û`, `ú`, `ü` | **`-`**, `–` (em dash), `–` (en dash), `∪` | _Page down_ |
| **`i`** | `î`, `í`, `ï`, `ì` | **`_`**, `\|` | _Page up_ |
| **`o`** | `ô`, `ó`, `ò`, `ö`, `õ` | **`/`**, `\`, `œ`, `º` (masc. ordinal), `÷` | _End_ |
| **`p`** |  | **`:`**, `;`, `¶` | _Escape_ |
| | | |
| **`a`** | `à`, `â`, `á`, `ä`, `ã` | **`@`**, `æ`, `ª`, `←` | _Left arrow_ |
| **`s`** |  | **`4`**, `ß`, `§`, `↓` | _Down arrow_ |
| **`d`** |  | **`5`**, `∂`, `→`, `⇒` | _Right arrow_ |
| **`f`** |  | **`6`**, `ƒ` | |
| **`g`** |  | **`*`**, `•`, `·` | |
| **`h`** |  | **`#`**, `²`, `♯` | _Left arrow_ |
| **`j`** |  | **`+`**, `=`, `≠`, `≈`, `±` | _Down arrow_ |
| **`k`** |  | **`"`**, `%`, `‰`, `‱` | _Up arrow_ |
| **`l`** |  | **`'`**, `` ` `` | _Right arrow_ |
| | | |
| **`z`** |  | **`!`**, `¡`, `‽` | _Tab_ |
| **`x`** |  | **`7`**, `×`, `χ` | _Cut_* |
| **`c`** |  | **`8`**, `©`, `¢`, `⊂`, `⊄`, `⊃`, `⊅` | _Copy_* |
| **`v`** |  | **`9`**, `∀`, `√` | _Paste_* |
| **` ` (space bar)** |  | `	` (tab), | _Shift modifier_ |
| **`b`** |  | **`.`**, `…`, `ß`, `∫`, `♭` | `$` |
| **`n`** |  | **`,`**, `ñ`, `¬`, `∩` | `=` |
| **`m`** |  | **`?`**, `$`, `€`, `£`, `¿` | `%` |

_* The Cut/Copy/Paste actions are only available when no modifier is pressed._

## `sym` modifier map

![`sym` modifier map](readme-symbehavior.png)

# Customizing and contributing

Feel free to adjust the layout to your needs by modifying the code and building your own version. If you think your changes could be useful to others, please consider contributing them back to this project or making a public fork.

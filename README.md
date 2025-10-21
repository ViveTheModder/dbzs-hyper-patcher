# dbzs-hyper-patcher
A patcher for DBZ Sparking! HYPER that fixes a variety of issues: **crashes, typos, audio, visuals**. Comes with both a **command-line** and **GUI** version, which includes **Portuguese (PT-BR) translation**. 

Either way, it will only work as intended on an **untouched (or already patched) copy of DBZ Sparking! HYPER**, which can be found on [its website](https://dbzs-hyper.nekoweb.org/).

# List of Patches
| Patch | Argument | Description | Release |
| ------ | ------ | ------ | ------ |
| Fix Game Crashes | fix-crash | Fixes Dragon History crash (which prevented "The World's Strongest" from being completed), Sim Dragon crash (which would occur shortly after selecting a character), Great Ape detransformation crash (by disabling it for all costumes), and Raditz crash (from his 1st damaged costume; only happens on console). | 26 Sep. - 28 Sep., 9 Oct.
| Fix Story Mode Typos | -fix-typos | Fixes Dragon History subtitle errors (such as misspellings and incomplete sentences). | 26 Sep.
| Fix Vegeta Victory Quote | -fix-vegeta | Disables Super Saiyan 2 Vegeta's wrongly assigned victory quote against Super Android #13. | 26 Sep.
| Fix Pikkon Permanent Halo | -fix-pikkon | Disables Pikkon's halo by default for his 2nd costume, as is the case for his 1st costume. | 28 Sep.
| Fix Kaio-ken Goku Face | -fix-goku | Fixes Kaio-ken Goku's 3rd damaged costume's extra faces to point to the right texture. | 29 Sep.
| Fix JP Buutenks Special Attack SFX | -fix-buutenks | Fixes the sound effects for Super Buu (Gotenks Absorbed)'s Finish Sign and Super Buu Kamikaze Attack when Japanese voices are selected. | 3 Oct.
| Fix Krillin Spirit Bomb | -fix-krillin | Fixes the damage of Krillin's Spirit Bomb to be in line with other Spirit Bombs. | 6 Oct.
| Fix Sim Dragon Random Z-Items | -fix-sim | Prevents removed Z-Items from showing up in Sim Dragon. | 15 Oct.
| Fix Gohan Blast Combos | -fix-gohan | Fixes the Blast Combos for base Gohan's Dragon Tornado and Burst Meteor. | 22 Oct.
| Fix Majin Buu Blaster Wave | -fix-buu | Replaces Majin Buu's incorrect Kiai Cannon with the intended Blaster Wave. | 22 Oct.
| Fix All | -fix-all | Applies all the patches listed above. | 26 Sep. - 22 Oct.

# How to Use
## Command-line
1. Navigate to the folder where the tool is downloaded, go to the address bar, type ``cmd`` and press Enter.
<img width="1408" height="742" alt="image" src="https://github.com/user-attachments/assets/abad76d2-02b1-40cd-ac89-8542f6c959bf" />

2. Type the following, then press Enter: ``java -jar dbzs-hyper-patcher.jar -h``.
<img width="1126" height="762" alt="image" src="https://github.com/user-attachments/assets/7fd70c8b-572e-4100-8308-138787b5888d" />

3. Following the example below, specify the path to the ISO, as well as the patch type. Patch types are ordered chronologically, but ``-fix-all`` is strongly encouraged.
<img width="1126" height="762" alt="image" src="https://github.com/user-attachments/assets/346c0714-584f-4081-a5a8-9b8c2654f8f3" />

## GUI
1. Double-click ``dbzs-hyper-patcher.jar``.
<img width="1408" height="742" alt="image" src="https://github.com/user-attachments/assets/1520aa04-75d6-43f5-a3f3-2dff19cbfec8" />

2. Choose the patch type from the dropdown menu ~~(or JComboBox)~~. For explanation on the chosen patch type, hover over it for a tooltip to show up.
<img width="1408" height="742" alt="image" src="https://github.com/user-attachments/assets/17b17628-1590-43ad-a2b0-13be92f181a0" />

3. Click the ``Apply Patch`` button and choose the untouched (or already patched) copy of DBZ Sparking! HYPER.
<img width="944" height="632" alt="image" src="https://github.com/user-attachments/assets/5bf591a3-2431-43b3-ae9b-dfee1ca5e962" />

<img width="1118" height="748" alt="image" src="https://github.com/user-attachments/assets/f556ee3f-1965-48c7-9727-f7056b8c4bcf" />

<img width="329" height="151" alt="image" src="https://github.com/user-attachments/assets/a80f90ce-f54a-4bbd-aa6a-f3eaf215f1a3" />


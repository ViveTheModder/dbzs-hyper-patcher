# dbzs-hyper-patcher
A patcher for DBZ Sparking! HYPER that fixes a variety of issues: **crashes, typos, audio, visuals**. Comes with both a **command-line** and **GUI** version, which includes **Portuguese (PT-BR) and Spanish (ES-ES) translation**. 

Either way, it will only work as intended on an **untouched (or already patched) copy of DBZ Sparking! HYPER**, which can be found on [its website](https://dbzs-hyper.nekoweb.org/).

# List of Patches
| Patch | Argument | Description | Release |
| ------ | ------ | ------ | ------ |
| Fix Game Crashes | fix-crash | Fixes Dragon History crash (which prevented "The World's Strongest" from being completed), Sim Dragon crash (which would occur shortly after selecting a character), Great Ape detransformation crash (by disabling it for all costumes), Raditz crash (from his 1st damaged costume; only happens on console), and Goku Spirit Bomb crash (only against Meta-Cooler). | 26 Sep. - 28 Sep., 9 Oct., 23 Oct.
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
<img width="1408" height="806" alt="image" src="https://github.com/user-attachments/assets/1a915db7-5f5b-4fe8-8fab-0716cfdff58d" />

<img width="1408" height="806" alt="image" src="https://github.com/user-attachments/assets/6b6e2c6d-3327-4cbc-b11b-fd6216cefb1f" />

2. Type the following, then press Enter: ``java -jar dbzs-hyper-patcher.jar -h``.
<img width="1483" height="762" alt="image" src="https://github.com/user-attachments/assets/02b50795-0340-4e9f-93d8-80cf2b16cd5b" />

3. Following the example below, specify the path to the ISO, as well as the patch type. Patch types are ordered chronologically, but ``-fix-all`` is strongly encouraged.
<img width="1483" height="762" alt="image" src="https://github.com/user-attachments/assets/9eb324b8-522e-43d9-87ac-d69ab7132dd5" />

## GUI
1. Double-click ``dbzs-hyper-patcher.jar``.
<img width="1408" height="806" alt="image" src="https://github.com/user-attachments/assets/355640a9-eb9d-4e37-823b-5dd90ded4011" />

2. Choose the patch type from the dropdown menu ~~(or JComboBox)~~. For explanation on the chosen patch type, hover over it for a tooltip to show up.
<img width="944" height="632" alt="image" src="https://github.com/user-attachments/assets/20e350a4-b977-41f6-9e8f-595c7e7f73f4" />

3. Go to ``File`` -> ``Open ISO...`` and choose the untouched (or already patched) copy of DBZ Sparking! HYPER.
<img width="944" height="632" alt="image" src="https://github.com/user-attachments/assets/d2ca2b05-409e-40a7-a85b-0b4cccd40d0f" />

<img width="765" height="537" alt="image" src="https://github.com/user-attachments/assets/b488ef55-7889-4dc6-9863-a1ecd7320a6e" />

4. Click the ``Apply Patch`` button.
<img width="944" height="632" alt="image" src="https://github.com/user-attachments/assets/6ddb11f8-a506-4cd3-aab3-6046998d3863" />

<img width="942" height="1020" alt="image" src="https://github.com/user-attachments/assets/261ca8a2-7d25-4ee5-bc30-4ab0c05bcc1e" />

**NOTE 1:** The ISO cannot be patched twice, unless Step 3 is repeated.

<img width="329" height="151" alt="image" src="https://github.com/user-attachments/assets/fb62a918-037b-4251-8656-7516ebb3720a" />

**NOTE 2:** And obviously, if no ISO is detected, the user will be warned.

<img width="329" height="151" alt="image" src="https://github.com/user-attachments/assets/16c2216f-f10b-4391-aa2a-0443c8a7d4e2" />


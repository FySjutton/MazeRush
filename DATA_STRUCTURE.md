# Map - Data Structure
The data structure of a MazeRush map is quite simple.
```
/MazeRush
    /maps
        /[name]   // the name of the map
            /4_way
                variant1.nbt
                variant2.nbt
            /3_way
                variant1.nbt
                variant2.nbt
            /straight
                variant1.nbt
                variant2.nbt
            /corner
                variant1.nbt
                variant2.nbt
            /deadend
                variant1.nbt
                variant2.nbt
            spawn.nbt
            data.json // this file contains the information about the map, see below
```

## data.json
```json5
{
  "map": "Avoxy", // name of the map
  "creator": "Fy17", // who the map was made by
  "room_size": 9, // the size of a room, in blocks (9x9)
  "map_size": 12, // the size of the map, amount of rooms (12x12, 144 rooms)
  "loop_chance": 0.12, // a measurement of the amount of "openness", see preview file
  
  // a list on all the files, their variants and their chances of spawning
  "files": {
    "4_way": [ // a list of the different variations
      {
        "name": "variation1.nbt", // the name of the variation
        "chance": 0.7, // the chance of this variation of spawning
        "spawnable": true // if players should be able to spawn in it from the start
      },
      {
        "name": "variation2.nbt",
        "chance": 0.3,
        "spawnable": true
      }
    ],
    "3_way": [
      {
        "name": "variation1.nbt",
        "chance": 1,
        "spawnable": true
      }
    ],
    "straight": [
      {
        "name": "variation1.nbt",
        "chance": 1,
        "spawnable": true
      }
    ],
    "corner": [
      {
        "name": "variation1.nbt",
        "chance": 1,
        "spawnable": true
      }
    ],
    "deadend": [
      {
        "name": "variation1.nbt",
        "chance": 1,
        "spawnable": true
      }
    ]
  }
}
```

## The nbt files
The map is made up of multiple nbt files, that the plugin then generates a custom maze from.
Build each variation in the same size, and make sure their doorways fits perfectly.
Place a structure block directly below the NW-corner on the build:
- Put it in "SAVE" mode
- Enter relatives "0 1 0"
- Enter structure size, for example "9 20 9"

You can then press the right SAVE button in order to save it as a file on the disk, copy it and put it like the structure seen above.

## Spawn NBT
This is a 3x3-room empty space in the middle of the map, intended as the goal or where you leave your crystals. The middle on each edge requires a doorway.
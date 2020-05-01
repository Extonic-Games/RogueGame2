
var tiles = import("tiles");

export default class Room {
    constructor(width, height) {
        this.size = {x: width, y: height};
        this.pos = {x: 0, y: 0};
        this.tiles = [];

        for (let y = 0; y < this.size.y; y++) {
            let row = [];
            for (let x = 0; x < this.size.x; x++) {
            	if (y === 0 || y === this.size.y - 1 || x === 0 || x === this.size.x - 1) {
            		row.push(tiles.wall);
            	} else {
            		row.push(tiles.floor);
            	}
            }
            this.tiles.push(row);
        }
    }

    getDoorLocations() {
    	let doors = [];
    	for (let y = 0; y < this.size.y; y++) {
    		for (let x = 0; x < this.size.x; x++) {
    			if (this.tiles[y][x] === tiles.door) {
    				doors.push({x: x, y: y});
    			}
    		}
    	}

    	return doors;
    }

    static areConnected(room1, room2) {
    	let doors = room1.getDoorLocations();
    	for (let i = 0; i < doors.length; i++) {
    		let d = doors[i];

    		d.x += room1.pos.x;
    		d.y += room1.pos.y;

    		d.x -= room2.pos.x;
    		d.y -= room2.pos.y;

    		if (d.x < 0 || d.x > room2.size.x - 1 || d.y < 0 || d.y > room2.size.y - 1) {
    			continue;
    		}

    		if (room2.tiles[d.y][d.x] === this.door) {
    			return true;
    		}
    	}

    	return false;
    }

}
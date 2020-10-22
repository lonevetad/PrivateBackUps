<template>
	<div>
		<label>
			Angle of rotation (in degrees):
			<input
				type="number"
				min="0.0"
				max="360.0"
				step="5.0"
				v-model="angle"
			/>
			<div>{{angleRad}}</div>
		</label>
		<div id="pointPanel" ref="pointPanel" @click="onClickPoint" :style="pointPanelSizeStyle">
			<div class="daPoint" v-for="(p, i) in pointsRotated" :key="i" :style="pointToStyle(p)"></div>
		</div>
	</div>
</template>
<script lang="ts">
import { Vue, Component } from 'vue-class-decorator';
import { todos } from "@/vuex";


type Point2D = {
	x: number;
	y: number;
};

@Component({
	components: {
	}
})
export default class MatrixRotationPage extends Vue {
	private points: Point2D[] = [];
	private ang: number = 0.0;
	private angleRad: number = 0.0;
	private rotationMatrix: number[][] = [[1, 0], [0, 1]];

	mounted() {
		var shift: number[];
		var xs: number, ys: number;
		shift = this.centerShift;
		xs = shift[0];
		ys = shift[1];
		this.points = [
			{ x: xs, y: ys },
			{ x: xs + 100, y: ys - 175 },
			{ x: xs - 130, y: ys - 80 },
			{ x: xs - 50, y: ys - 50 },
			{ x: xs + 50, y: ys - 50 },
			{ x: xs - 70, y: ys + 45 },
			{ x: xs - 60, y: ys + 50 },
			{ x: xs - 50, y: ys + 55 },
			{ x: xs - 40, y: ys + 60 },
			{ x: xs - 30, y: ys + 60 },
			{ x: xs - 20, y: ys + 65 },
			{ x: xs - 10, y: ys + 65 },
			{ x: xs - 0, y: ys + 65 },
			{ x: xs + 10, y: ys + 65 },
			{ x: xs + 20, y: ys + 65 },
			{ x: xs + 30, y: ys + 60 },
			{ x: xs + 40, y: ys + 60 },
			{ x: xs + 50, y: ys + 55 },
			{ x: xs + 60, y: ys + 50 },
			{ x: xs + 70, y: ys + 45 },
		];
	}

	//


	onClickPoint(even: MouseEvent): void {
		var elms =
			this.$refs["pointPanel"]; // Vue | Element | Vue[] | Element[]
		var offsetx = 0, offsety = 0;
		if (elms instanceof Vue) {
			//offset = (elms as Vue).
			console.log("its a Vue");
		} else if (elms instanceof Element) {
			var getBoundingClientRect = null;
			//var style = (elms as Element).getAttribute("style");
			getBoundingClientRect = (elms as Element).getBoundingClientRect()//.getAttribute("style");
			offsetx = getBoundingClientRect.left;
			offsety = getBoundingClientRect.top;
		} else {
			console.log("its a boh");
		}
		this.addPoint(even.pageX - offsetx, even.pageY - offsety);
	}

	containsPoint(p: Point2D): boolean {
		for (var pp of this.points) {
			if (pp.x == p.x && pp.y == p.y) return true;
		}
		return false;
	}
	addPoint(x: number, y: number): void {
		var p: Point2D;
		p = { x, y };
		if (!this.containsPoint(p))
			this.points.push(p);
	}

	get angle(): number {
		return this.ang;
	}

	set angle(aa: number) {
		var s, c, a: number;
		this.ang = aa;
		a = (aa * Math.PI) / 180.0;
		this.angleRad = a;
		s = Math.sin(a);
		c = Math.cos(a);
		this.rotationMatrix = [
			[c, -s],
			[s, c]
		];
	}

	get pointPanelSize(): number[] {
		return [500, 500];
	}

	get pointPanelSizeStyle(): string {
		var panelSize: number[];
		var h: number, w: number;
		panelSize = this.pointPanelSize;
		w = panelSize[0];
		h = panelSize[1];
		return "overflow: auto; position: relative; border: 2px solid blue; width: " + w + "px; height: " + h + "px"
			+ " min-width: " + w + "px; min-height: " + h + "px";
	}

	pointToStyle(p: Point2D): string {
		return "width: 2px; height: 2px; position: absolute; border: 2px solid orange; " +
			"left: " + p.x + "px; top: " + p.y + "px;";
	}

	//

	get centerShift(): number[] {
		var panelSize: number[];
		panelSize = this.pointPanelSize;
		return [
			panelSize[0] / 2,
			panelSize[1] / 2
		];
	}

	get pointsRotated(): Point2D[] {
		var pts: number[][];
		var pr: Point2D[];
		var pointsRotatedAsNumber: number[];
		var p: Point2D;
		var i, len;
		var shift: number[];
		var xs: number, ys: number;
		shift = this.centerShift;
		xs = shift[0];
		ys = shift[1];
		// conversion point to number AND shift relatively to center
		pts = [];
		len = this.points.length;
		i = -1;
		while (++i < len) {
			p = this.points[i];
			pts.push([p.x - xs, p.y - ys]);
		}
		pts = this.dotProduct(pts, this.rotationMatrix);
		// conversion back from number BEFIRE shift relatively to center
		if (pts.length == 0) return [];
		pr = [];
		len = pts.length;
		i = -1;
		while (++i < len) {
			pointsRotatedAsNumber = pts[i];
			pr.push({ x: pointsRotatedAsNumber[0] + xs, y: pointsRotatedAsNumber[1] + ys });
		}
		return pr;
	}


	//

	dotProduct(a: number[][], b: number[][]): number[][] {
		var m: number, n: number, o: number, ra: number, cb: number, i: number, temp: number;
		var newRow: number[], rowATemp: number[];
		var ret: number[][];
		if ((m = a.length) == 0 || (n = b.length) == 0 || (a[0].length != n)) return [];
		o = b[0].length;
		ret = []; // will have length == m
		ra = -1;
		while (++ra < m) {
			newRow = []; // will have length == o
			ret.push(newRow);
			rowATemp = a[ra];
			cb = -1;
			while (++cb < o) {
				temp = 0.0;
				i = -1;
				while (++i < n) {
					temp += rowATemp[i] * b[i][cb];
				}
				newRow.push(temp);
			}
		}
		return ret;
	}
}
</script>
<style lang="scss" scoped>
.daPoint {
	width: 2px;
	height: 2px;
	/*
    border: 2px solid orange;
    */
	position: relative;
}
/*
#pointPanel {
	border: 2px solid blue;
}*/
</style>
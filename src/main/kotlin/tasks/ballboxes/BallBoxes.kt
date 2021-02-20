package tasks.ballboxes

import models.Experiment
import models.Hypothesis
import models.SolveBuilder
import models.utils.FastScanner

class BallBoxes : SolveBuilder {
    var n_boxes = 6
    var m = 5
    var d = 3
    var p_change_box = 0.100
    var nExp = 10000
    var list = mutableListOf<Experiment>()
    var boxes = mutableListOf<Box>()
    val hypo = mutableListOf<Hypothesis>()

    override fun read() {
        val j = this::class.java
        val scanner = FastScanner(j.getResourceAsStream("/tasks.ballboxes\\\\task_1_ball_boxes.txt"))
        parseBox(scanner)
        parseExp(scanner)    }

    override fun buildHypo(): List<Hypothesis> {
        val p = 1.0 * (1 - p_change_box) / n_boxes
        println(p)
        for (box in boxes) {
            hypo.add(Hypo(p, box, 1 - p_change_box))
        }
        return hypo
    }

    private fun parseBox(scn: FastScanner) {
        var total = 3
        var red = 3
        var white = 3
        var black = 3
        var green = 3
        var blue = 3
        var i = 0
        var c = 0
        while (i < n_boxes) {
            when (scn.next()) {
                "Total:" -> total = scn.next().replace(".", "").toInt()
                "Red:" -> red = scn.next().replace(",", "").toInt()
                "White:" -> white = scn.next().replace(",", "").toInt()
                "Black:" -> black = scn.next().replace(",", "").toInt()
                "Green:" -> green = scn.next().replace(",", "").toInt()
                "Blue:" -> blue = scn.nextInt()
                else -> c--
            }
            c++
            if (c == m + 1) {
                boxes.add(Box(total, red, white, black, green, blue))
                c = 0
                i++
            }
        }
    }

    private fun parseExp(scn: FastScanner) {
        var current = BoxExp()
        var i = 0
        var c = 0
        while (i < nExp) {
            when (scn.next().replace(",", "")) {
                "Red" -> current.list.add(BoxExp.Color.RED)
                "White" -> current.list.add(BoxExp.Color.WHITE)
                "Black" -> current.list.add(BoxExp.Color.BLACK)
                "Green" -> current.list.add(BoxExp.Color.GREEN)
                "Blue" -> current.list.add(BoxExp.Color.BLUE)
                else -> c--
            }
            c++
            if (c == d) {
                list.add(current)
                current = BoxExp()
                c = 0
                i++
            }
        }
    }
}
package ballboxes

import model.Experiment
import model.Hypothesis
import model.Property
import model.Solver
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler

class Solver(hypo: List<Hypothesis>, exps: List<Experiment>, property: Property) : Solver(hypo, exps, property) {
    override fun run() {
        // 1.a
        val allHypos = countPost()
        drawAll(allHypos, 50, "1.a")

        // 1.b
        var max = 0.0
        var ind = 0
        for ((i, h) in allHypos.withIndex()) {
            if (h.last() > max) {
                ind = i
                max = h.last()
            }
            if (max == 1.0) {
                break
            }
        }
        draw(allHypos[ind], 50, "1.b", "hypo$ind")

        // 1.c
        var count = 0
        val counter = mutableListOf<Int>()
        for (i in 0 until allHypos.first().size) {
            for (h in allHypos) {
                if (h[i] > 0.00001) {
                    count++
                }
            }
            counter.add(count)
            count = 0
        }
        draw(counter, 500, "1.c")

        // 2.a
        val psTeor = mutableListOf<Map<BoxExp.Color, Double>>()
        for (h in hypo) {
            val box = h.info as Box
            val map = mutableMapOf<BoxExp.Color, Double>()
            map[BoxExp.Color.RED] = box.red.toDouble() / box.total
            map[BoxExp.Color.WHITE] = box.white.toDouble() / box.total
            map[BoxExp.Color.BLACK] = box.black.toDouble() / box.total
            map[BoxExp.Color.GREEN] = box.green.toDouble() / box.total
            map[BoxExp.Color.BLUE] = box.blue.toDouble() / box.total
            psTeor.add(map)
        }

        val psExp = mutableMapOf<BoxExp.Color, Double>()
        val drawer = MutableList(5) { mutableListOf<Double>() }
        var countR = 0       // количество эксперементов с красными шарами
        var countW = 0       // количество эксперементов с белыми шарами
        var countB = 0       // количество эксперементов с черными шарами
        var countG = 0       // количество эксперементов с зелеными шарами
        var countBl = 0      // количество эксперементов с голубыми шарами

        for ((size, e) in exps.withIndex()) {
            val ex = e as BoxExp
            countR += ex.list.count { it == BoxExp.Color.RED }
            countW += ex.list.count { it == BoxExp.Color.WHITE }
            countB += ex.list.count { it == BoxExp.Color.BLACK }
            countG += ex.list.count { it == BoxExp.Color.GREEN }
            countBl += ex.list.count { it == BoxExp.Color.BLUE }
            if (size < 500) {
                drawer[0].add(countBl.toDouble() / ((size + 1) * 3))
                drawer[1].add(countG.toDouble() / ((size + 1) * 3))
                drawer[2].add(countR.toDouble() / ((size + 1) * 3))
                drawer[3].add(countW.toDouble() / ((size + 1) * 3))
                drawer[4].add(countB.toDouble() / ((size + 1) * 3))
            }

        }
        psExp[BoxExp.Color.RED] = countR.toDouble() / (exps.size * 3)
        psExp[BoxExp.Color.WHITE] = countW.toDouble() / (exps.size * 3)
        psExp[BoxExp.Color.BLACK] = countB.toDouble() / (exps.size * 3)
        psExp[BoxExp.Color.GREEN] = countG.toDouble() / (exps.size * 3)
        psExp[BoxExp.Color.BLUE] = countBl.toDouble() / (exps.size * 3)

        // 2.b
        printT(psTeor)
        println(psExp.toString())

        // 2.c
        drawExp(drawer)

    }

    fun printT(list: List<Map<BoxExp.Color, Double>>) {
        for (l in list) {
            println(l.toString())
        }
    }

    fun drawExp(ys: List<List<Number>>) {
        val chart =
            XYChartBuilder().width(800).height(600)
                .theme(Styler.ChartTheme.Matlab)
                .title("2.c")
                .xAxisTitle("nExp").yAxisTitle("pExp")
                .build()

        val size = ys.first().size
        val x = MutableList(size) { index -> (index + 1) * 3 }

        for ((i, y) in ys.withIndex()) {
            chart.addSeries(getName(i), x, y)
        }

        SwingWrapper(chart).displayChart()
    }

    private fun getName(color: Int): String {
        return when (color) {
            0 -> "Blue"
            1 -> "Green"
            2 -> "Red"
            3 -> "White"
            4 -> "Black"
            else -> "null"
        }
    }

    override fun changePA() {
        pA = 0.0
        for (h in hypo) {
            pA += h.p * h.pAH * 0.9
        }
    }
}
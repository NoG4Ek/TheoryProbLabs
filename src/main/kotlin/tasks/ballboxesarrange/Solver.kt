package tasks.ballboxesarrange

import models.*
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler

class Solver(hypoArrange: List<List<Hypothesis>>, exps: List<Experiment>, property: Property) : SolverArrange(hypoArrange, exps, property) {
    override fun run() {
        // 2.a
        val allHypos = countPost()
        for ((i, hypo) in allHypos.withIndex()) {
            drawAll(hypo, 50, "2.a - $i")
        }

        // 2.b
        for ((j, hypo) in allHypos.withIndex()) {
            var max = 0.0
            var ind = 0
            for ((i, h) in hypo.withIndex()) {
                if (h.last() > max) {
                    ind = i
                    max = h.last()
                }
                if (max == 1.0) {
                    break
                }
            }
            draw(hypo[ind], 50, "2.b - $j", "hypo$ind")
        }

        // МНЕ СТЫДНО ЗА ТО, ЧТО БУДЕТ ДАЛЬШЕ
        // HARDCODE
        // 1.a
        fun factorial(num: Int) = (1..num).reduce { a, b -> a * b }
        val countHypos = factorial(hypoArrange.size)
        val hypoArrangeForEveryKit = MutableList(countHypos) { MutableList(allHypos[0][0].size * 6) { 1.0 / (hypoArrange.size) } }

        var ind = 0
        for ((b0i, b0) in allHypos[0].withIndex()) {
            for ((b1i, b1) in allHypos[1].withIndex()) {
                for ((b2i, b2) in allHypos[2].withIndex()) {
                    for ((b3i, b3) in allHypos[3].withIndex()) {
                        for ((b4i, b4) in allHypos[4].withIndex()) {
                            for ((b5i, b5) in allHypos[5].withIndex()) {
                                if (b0i != b1i && b0i != b2i && b0i != b3i && b0i != b4i && b0i != b5i &&
                                        b1i != b2i && b1i != b3i && b1i != b4i && b1i != b5i &&
                                        b2i != b3i && b2i != b4i && b2i != b5i &&
                                        b3i != b4i && b3i != b5i &&
                                        b4i != b5i) {
                                    //val ind = (b0i * 120) + (b1i * 24) + (b2i * 6) + (b3i * 2) + (b4i) + b5i
                                        if (ind == 264) {
                                            println("$b0i + $b1i + $b2i + $b3i + $b4i + $b5i")
                                        }
                                    for (exp in 1..(allHypos[0][0].size / 6)) {
                                        hypoArrangeForEveryKit[ind][exp] =
                                            (b0[exp] + b1[exp] + b2[exp] + b3[exp] + b4[exp] + b5[exp]) / 6
                                    }
                                    ind++
                                }
                            }
                        }
                    }
                }
            }
        }
        drawAll(hypoArrangeForEveryKit, 80, "1.a")

        var bestInd = 0
        var max = 0.0
        for ((i, kit) in hypoArrangeForEveryKit.withIndex()) {
            for (j in kit)
            if (j >= max) {
                max = j
                bestInd = i
            }
        }
        draw(hypoArrangeForEveryKit[bestInd], 80, "1.b", "hypo$bestInd")

        // 1.c
        var count = 0
        val counter = mutableListOf<Int>()
        for (i in 0 until allHypos[0][0].size) {
            for (h in hypoArrangeForEveryKit) {
                if (h[i] > 0.00001) {
                    count++
                }
            }
            counter.add(count)
            count = 0
        }
        draw(counter, 250, "1.c")


        // 2.a
        val psTeor = MutableList(6) { mutableListOf<Map<BoxExp.Color, Double>>() }
        for ((j, hypo) in hypoArrange.withIndex()) {
            for (h in hypo) {
                val box = h.info as Box
                val map = mutableMapOf<BoxExp.Color, Double>()
                map[BoxExp.Color.RED] = box.red.toDouble() / box.total
                map[BoxExp.Color.WHITE] = box.white.toDouble() / box.total
                map[BoxExp.Color.BLACK] = box.black.toDouble() / box.total
                map[BoxExp.Color.YELLOW] = box.yellow.toDouble() / box.total
                map[BoxExp.Color.GREEN] = box.green.toDouble() / box.total
                map[BoxExp.Color.BLUE] = box.blue.toDouble() / box.total
                psTeor[j].add(map)
            }
        }

        val psExp = MutableList(6) { mutableMapOf<BoxExp.Color, Double>() }
        val drawer = MutableList(6) { MutableList(6) { mutableListOf<Double>() } }
        val countR = IntArray(6) { 0 }       // количество эксперементов с красными шарами
        val countW = IntArray(6) { 0 }      // количество эксперементов с белыми шарами
        val countB = IntArray(6) { 0 }       // количество эксперементов с черными шарами
        val countY = IntArray(6) { 0 }      // количество эксперементов с желтыми шарами
        val countG = IntArray(6) { 0 }       // количество эксперементов с зелеными шарами
        val countBl = IntArray(6) { 0 }     // количество эксперементов с голубыми шарами

        for ((size, e) in exps.withIndex()) {
            val ex = e as BoxExp
            countR[size%6] += ex.list.count { it == BoxExp.Color.RED }
            countW[size%6] += ex.list.count { it == BoxExp.Color.WHITE }
            countB[size%6] += ex.list.count { it == BoxExp.Color.BLACK }
            countY[size%6] += ex.list.count { it == BoxExp.Color.YELLOW }
            countG[size%6] += ex.list.count { it == BoxExp.Color.GREEN }
            countBl[size%6] += ex.list.count { it == BoxExp.Color.BLUE }
            if (size < 500) {
                for ((i, dr) in drawer.withIndex()) {
                    drawer[i][0].add(countBl[i].toDouble() / ((size + 1).toDouble() / 6 * 4))
                    drawer[i][1].add(countG[i].toDouble() / ((size + 1).toDouble() / 6 * 4))
                    drawer[i][2].add(countR[i].toDouble() / ((size + 1).toDouble() / 6 * 4))
                    drawer[i][3].add(countW[i].toDouble() / ((size + 1).toDouble() / 6 * 4))
                    drawer[i][4].add(countB[i].toDouble() / ((size + 1).toDouble() / 6 * 4))
                    drawer[i][5].add(countY[i].toDouble() / ((size + 1).toDouble() / 6 * 4))
                }
            }

        }
        for (i in psExp.indices) {
            psExp[i][BoxExp.Color.RED] = countR[i].toDouble() / (exps.size.toDouble() / 6 * 4)
            psExp[i][BoxExp.Color.WHITE] = countW[i].toDouble() / (exps.size.toDouble() / 6 * 4)
            psExp[i][BoxExp.Color.BLACK] = countB[i].toDouble() / (exps.size.toDouble() / 6 * 4)
            psExp[i][BoxExp.Color.YELLOW] = countY[i].toDouble() / (exps.size.toDouble() / 6 * 4)
            psExp[i][BoxExp.Color.GREEN] = countG[i].toDouble() / (exps.size.toDouble() / 6 * 4)
            psExp[i][BoxExp.Color.BLUE] = countBl[i].toDouble() / (exps.size.toDouble() / 6 * 4)
        }

        // 2.b
        printT(psTeor)
        printE(psExp)

        // 2.c
        for ((i, dr) in drawer.withIndex()) {
            drawExp(dr, i)
        }

    }

    fun printE(list: MutableList<MutableMap<BoxExp.Color, Double>>) {
        for (l in list) {
            println(l.toString())
        }
    }

    fun printT(list: List<List<Map<BoxExp.Color, Double>>>) {
        for (h in list) {
            for (b in h) {
                println(b.toString())
            }
            println()
        }
    }

    fun drawExp(ys: List<List<Number>>, n: Int) {
        val chart =
            XYChartBuilder().width(800).height(600)
                .theme(Styler.ChartTheme.Matlab)
                .title("3.c - $n")
                .xAxisTitle("nExp").yAxisTitle("pExp")
                .build()

        val size = ys.first().size
        val x = MutableList(size) { index -> (index + 1) * 3 }

        for ((i, y) in ys.withIndex()) {
            chart.addSeries(getNeme(i), x, y)
        }

        SwingWrapper(chart).displayChart()
    }

    fun getNeme(color: Int): String {
        return when (color) {
            0 -> "Blue"
            1 -> "Green"
            2 -> "Red"
            3 -> "White"
            4 -> "Black"
            5 -> "Yellow"
            else -> "null"
        }
    }
}
package models

import java.io.PrintWriter
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler


abstract class SolverArrange(
    var hypoArrange: List<List<Hypothesis>>, val exps: List<Experiment>,
    val property: Property = Property("output.txt")
) {

    val out = PrintWriter(property.fileName)
    var pA = 0.0

    abstract fun run()

    open fun countPost(): List<List<List<Double>>> {
        val allHypos = MutableList(hypoArrange.size) { MutableList(hypoArrange[0].size) { mutableListOf<Double>() } }
        for ((j, hypo) in hypoArrange.withIndex()) {
            allHypos[j].putHypos(hypo)
        }

            for ((i, exp) in exps.withIndex()) {
                val curBoxHypo = hypoArrange[(i)%(hypoArrange.size)]
                println("$i")
                changeAllPAH(curBoxHypo, exp)
                changePA(curBoxHypo)
                for (h in curBoxHypo) {
                    h.changeP(pA)
                }
                allHypos[(i)%(hypoArrange.size)].putHypos(curBoxHypo)
            }

        //out.close()
        return allHypos
    }

    open fun changePA(hypo: List<Hypothesis>) {
        pA = 0.0
        for (h in hypo) {
            pA += h.p * h.pAH
        }
    }

    fun changeAllPAH(hypo: List<Hypothesis>, exp: Experiment) {
        for (h in hypo) {
            h.changePAH(exp)
        }
    }

    fun draw(x: List<Int>, y: List<Number>, title: String, name: String = "f(x)") {
        val chart =
            XYChartBuilder().width(800).height(600)
                .theme(Styler.ChartTheme.Matlab)
                .title(title)
                .xAxisTitle("exp").yAxisTitle("p")
                .build()

        chart.addSeries(name, x, y)
        SwingWrapper(chart).displayChart()
    }

    fun draw(y: List<Number>, size: Int, title: String, name: String = "f(x)") {
        val x = MutableList(size) { index -> index }
        var yNew: List<Number> = y
        if (y.size != size) {
            yNew = y.subList(0, size)
        }
        draw(x, yNew, title, name)
    }

    open fun drawAll(x: List<Int>, ys: List<List<Number>>, size: Int, title: String, name: String = "f(x)") {
        val chart =
            XYChartBuilder().width(800).height(600)
                .theme(Styler.ChartTheme.Matlab)
                .title(title)
                .xAxisTitle("exp").yAxisTitle("p")
                .build()

        var xNew = x
        if (x.size != size) {
            xNew = x.subList(0, size)
        }

        for ((i, y) in ys.withIndex()) {
            val yNew = y.subList(0, size) //.getP()
            chart.addSeries("hypo$i", xNew, yNew)
        }

        SwingWrapper(chart).displayChart()
    }

    open fun drawAll(ys: List<List<Number>>, size: Int, title: String, name: String = "f(x)") {
        val x = MutableList(size) { index -> index }
        drawAll(x, ys, size, title, name)
    }

    open fun drawHypo(matrix: List<List<Double>>) {
        val chart =
            XYChartBuilder().width(800).height(600)
                .theme(Styler.ChartTheme.Matlab)
                .title(javaClass.simpleName)
                .xAxisTitle("exp").yAxisTitle("p")
                .build()

        val stop = if (property.stopDraw > matrix.first().size) {
            matrix.first().size
        } else {
            property.stopDraw
        }

        val x = MutableList(stop) { index -> index }

        for ((i, hypos) in matrix.withIndex()) {
            val y = hypos.subList(0, stop) //.getP()
            chart.addSeries("hypo$i", x, y)
        }

        SwingWrapper(chart).displayChart()
    }

    enum class Type {
        FILE, CONCOLE
    }

//    Higher order functions

    open fun MutableList<MutableList<Double>>.putHypos(hypos: List<Hypothesis>) {
        for ((i, h) in hypos.withIndex()) {
            this[i].add(h.p)
        }
    }
}
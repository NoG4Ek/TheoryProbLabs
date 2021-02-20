package model

import java.io.PrintWriter
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler


abstract class Solver(
    var hypo: List<Hypothesis>, val exps: List<Experiment>,
    val property: Property = Property("output.txt")
) {

    val out = PrintWriter(property.fileName)
    var pA = 0.0

    abstract fun run()

    open fun countPost(): List<List<Double>> {
        sout(-1, Type.FILE)
        val allHypos = MutableList(hypo.size) { mutableListOf<Double>() }
        allHypos.putHypos(hypo)

        for ((i, exp) in exps.withIndex()) {
            println("$i")
            changeAllPAH(exp)
            changePA()
            for (h in hypo) {
                h.changeP(pA)
            }
            allHypos.putHypos(hypo)
            sout(i, Type.FILE)
        }

        out.close()
        return allHypos
    }

    open fun countById(id: Int): List<Double> {
        val hypoId = mutableListOf<Double>()
        for (exp in exps) {
            changeAllPAH(exp)
            changePA()
            for (h in hypo) {
                h.changeP(pA)
            }
            hypoId.add(hypo[id].p)
        }
        return hypoId
    }

    var n = 0

    open fun countNext(): List<Double> {
        val nextExp = exps[n]
        val pList = mutableListOf<Double>()
        changeAllPAH(nextExp)
        changePA()
        for (h in hypo) {
            h.changeP(pA)
            pList.add(h.p)
        }
        n++
        return pList
    }

    fun reset() {
        n = 0
    }

    open fun changePA() {
        pA = 0.0
        for (h in hypo) {
            pA += h.p * h.pAH
        }
    }

    fun changeAllPAH(exp: Experiment) {
        for (h in hypo) {
            h.changePAH(exp)
        }
    }

    fun sout(i: Int, type: Type) {
        val bulid = StringBuilder()
        bulid.append("$i   \n  ")
        for (h in hypo) {
            bulid.append("$h  ")
        }
        bulid.append("\n")

        when (type) {
            Type.FILE -> out.write(bulid.toString())
            Type.CONCOLE -> println(bulid.toString())
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

    fun List<Hypothesis>.getP(): List<Double> {
        val list = mutableListOf<Double>()
        for (h in this) {
            list.add(h.p)
        }
        return list
    }
}
package models

abstract class Hypothesis(var p: Double, val info: Info) {
    open var pAH: Double = 0.0

    open fun changeP(pA: Double) {
        p = p * pAH / pA
    }

    abstract fun changePAH(exp: Experiment)


    override fun toString(): String {
        return "Hypothesis(p=$p) \n"
    }
}
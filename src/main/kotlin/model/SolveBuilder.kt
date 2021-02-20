package model

interface SolveBuilder {

    fun read()
    fun buildHypo(): List<Hypothesis>
}
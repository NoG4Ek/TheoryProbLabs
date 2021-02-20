package models

interface SolveBuilder {

    fun read()
    fun buildHypo(): List<Hypothesis>
}
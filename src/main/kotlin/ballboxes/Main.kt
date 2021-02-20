package ballboxes

import model.Property

fun main() {
    val property = Property("ballout.txt", 50)
    val builder = BallBoxes()
    builder.read()
    builder.buildHypo()
    val solver = Solver(builder.hypo, builder.list, property)
    solver.run()
}
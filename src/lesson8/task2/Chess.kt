@file:Suppress("UNUSED_PARAMETER")

package lesson8.task2

import kotlin.math.*

/**
 * Клетка шахматной доски. Шахматная доска квадратная и имеет 8 х 8 клеток.
 * Поэтому, обе координаты клетки (горизонталь row, вертикаль column) могут находиться в пределах от 1 до 8.
 * Горизонтали нумеруются снизу вверх, вертикали слева направо.
 */
data class Square(val column: Int, val row: Int) {
    /**
     * Пример
     *
     * Возвращает true, если клетка находится в пределах доски
     */
    fun inside(): Boolean = column in 1..8 && row in 1..8

    /**
     * Простая (2 балла)
     *
     * Возвращает строковую нотацию для клетки.
     * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
     * Для клетки не в пределах доски вернуть пустую строку
     */
    fun notation(): String {
        return if (inside()) "${('a' - 1 + column)}$row"
        else ""
    }
}

/**
 * Простая (2 балла)
 *
 * Создаёт клетку по строковой нотации.
 * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
 * Если нотация некорректна, бросить IllegalArgumentException
 */
fun square(notation: String): Square {
    if (!Regex("""[a-h][1-8]""").matches(notation)) throw IllegalArgumentException()
    return Square(notation[0].code - 96, notation[1].code - 48)
}

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматная ладья пройдёт из клетки start в клетку end.
 * Шахматная ладья может за один ход переместиться на любую другую клетку
 * по вертикали или горизонтали.
 * Ниже точками выделены возможные ходы ладьи, а крестиками -- невозможные:
 *
 * xx.xxххх
 * xх.хxххх
 * ..Л.....
 * xх.хxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: rookMoveNumber(Square(3, 1), Square(6, 3)) = 2
 * Ладья может пройти через клетку (3, 3) или через клетку (6, 1) к клетке (6, 3).
 */
fun rookMoveNumber(start: Square, end: Square): Int =
    when {
        !start.inside() || !end.inside() -> throw IllegalArgumentException()
        start == end -> 0
        start.row != end.row && start.column != end.column -> 2
        else -> 1
    }


/**
 * Средняя (3 балла)
 *
 * Вернуть список из клеток, по которым шахматная ладья может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов ладьи см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: rookTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможен ещё один вариант)
 *          rookTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(3, 3), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          rookTrajectory(Square(3, 5), Square(8, 5)) = listOf(Square(3, 5), Square(8, 5))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun rookTrajectory(start: Square, end: Square): List<Square> {
    val ans = mutableListOf(start)
    if (start.row != end.row) ans.add(Square(start.column, end.row))
    if (start.column != end.column) ans.add(end)
    return ans
}

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматный слон пройдёт из клетки start в клетку end.
 * Шахматный слон может за один ход переместиться на любую другую клетку по диагонали.
 * Ниже точками выделены возможные ходы слона, а крестиками -- невозможные:
 *
 * .xxx.ххх
 * x.x.xххх
 * xxСxxxxx
 * x.x.xххх
 * .xxx.ххх
 * xxxxx.хх
 * xxxxxх.х
 * xxxxxхх.
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если клетка end недостижима для слона, вернуть -1.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Примеры: bishopMoveNumber(Square(3, 1), Square(6, 3)) = -1; bishopMoveNumber(Square(3, 1), Square(3, 7)) = 2.
 * Слон может пройти через клетку (6, 4) к клетке (3, 7).
 */
fun bishopMoveNumber(start: Square, end: Square): Int =
    when {
        !start.inside() || !end.inside() -> throw IllegalArgumentException()
        start == end -> 0
        abs(start.row - start.column) % 2 != abs(end.row - end.column) % 2 -> -1
        abs(start.row - end.row) == abs(start.column - end.column) -> 1
        else -> 2
    }

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный слон может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов слона см. предыдущую задачу.
 *
 * Если клетка end недостижима для слона, вернуть пустой список.
 *
 * Если клетка достижима:
 * - список всегда включает в себя клетку start
 * - клетка end включается, если она не совпадает со start.
 * - между ними должны находиться промежуточные клетки, по порядку от start до end.
 *
 * Примеры: bishopTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          bishopTrajectory(Square(3, 1), Square(3, 7)) = listOf(Square(3, 1), Square(6, 4), Square(3, 7))
 *          bishopTrajectory(Square(1, 3), Square(6, 8)) = listOf(Square(1, 3), Square(6, 8))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun bishopTrajectory(start: Square, end: Square): List<Square> {
    val moves = bishopMoveNumber(start, end)
    if (moves == 0) return listOf(start)
    if (moves == 1) return listOf(start, end)
    if (moves == 2) {
        for (i in 1..8) {
            for (j in 1..8) {
                val temp = Square(i, j)
                if (bishopMoveNumber(start, temp) == 1 && bishopMoveNumber(temp, end) == 1)
                    return listOf(start, temp, end)
            }
        }
    }
    return listOf()
}

/**
 * Средняя (3 балла)
 *
 * Определить число ходов, за которое шахматный король пройдёт из клетки start в клетку end.
 * Шахматный король одним ходом может переместиться из клетки, в которой стоит,
 * на любую соседнюю по вертикали, горизонтали или диагонали.
 * Ниже точками выделены возможные ходы короля, а крестиками -- невозможные:
 *
 * xxxxx
 * x...x
 * x.K.x
 * x...x
 * xxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: kingMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Король может последовательно пройти через клетки (4, 2) и (5, 2) к клетке (6, 3).
 */
fun kingMoveNumber(start: Square, end: Square): Int =
    when {
        !start.inside() || !end.inside() -> throw IllegalArgumentException()
        start == end -> 0
        else -> max(abs(start.row - end.row), abs(start.column - end.column))
    }

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный король может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов короля см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: kingTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможны другие варианты)
 *          kingTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(4, 2), Square(5, 2), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          kingTrajectory(Square(3, 5), Square(6, 2)) = listOf(Square(3, 5), Square(4, 4), Square(5, 3), Square(6, 2))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun kingTrajectory(start: Square, end: Square): List<Square> {
    val ans = mutableListOf(start)
    val moves = kingMoveNumber(start, end)
    if (moves == 0) return ans
    else {
        var temp = start
        for (i in 1..moves) {
            val movesRow =
                if (abs(end.row - temp.row) > 0) (end.row - temp.row) / abs(end.row - temp.row) else 0
            val movesColumn =
                if (abs(end.column - temp.column) > 0) (end.column - temp.column) / abs(end.column - temp.column) else 0
            temp = Square(temp.column + movesColumn, temp.row + movesRow)
            ans.add(temp)
        }
    }
    return ans
}

/**
 * Сложная (6 баллов)
 *
 * Определить число ходов, за которое шахматный конь пройдёт из клетки start в клетку end.
 * Шахматный конь одним ходом вначале передвигается ровно на 2 клетки по горизонтали или вертикали,
 * а затем ещё на 1 клетку под прямым углом, образуя букву "Г".
 * Ниже точками выделены возможные ходы коня, а крестиками -- невозможные:
 *
 * .xxx.xxx
 * xxKxxxxx
 * .xxx.xxx
 * x.x.xxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: knightMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Конь может последовательно пройти через клетки (5, 2) и (4, 4) к клетке (6, 3).
 */

fun knightMoveNumber(start: Square, end: Square): Int {
    if (!start.inside() || !end.inside()) throw IllegalArgumentException()
    if (start == end) return 0
    val chessField = mutableMapOf<Square, Int>()
    for (i in 1..8) {
        for (j in 1..8) {
            chessField[Square(i, j)] = -1
        }
    }
    chessField[start] = 0
    var temp = 0
    while (chessField[end] == -1) {
        for ((key, value) in chessField) {
            if (value == temp) {
                val nextMoves = listOf(
                    Square(key.column + 2, key.row + 1),
                    Square(key.column + 2, key.row - 1),
                    Square(key.column - 2, key.row + 1),
                    Square(key.column - 2, key.row - 1),
                    Square(key.column + 1, key.row + 2),
                    Square(key.column + 1, key.row - 2),
                    Square(key.column - 1, key.row + 2),
                    Square(key.column - 1, key.row - 2)
                ).filter { chessField[it] == -1 }
                for (i in nextMoves) {
                    chessField[i] = temp + 1
                }
            }
        }
        temp++
    }
    return chessField[end]!!
}

/**
 * Очень сложная (10 баллов)
 *
 * Вернуть список из клеток, по которым шахматный конь может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов коня см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры:
 *
 * knightTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 * здесь возможны другие варианты)
 * knightTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(5, 2), Square(4, 4), Square(6, 3))
 * (здесь возможен единственный вариант)
 * knightTrajectory(Square(3, 5), Square(5, 6)) = listOf(Square(3, 5), Square(5, 6))
 * (здесь опять возможны другие варианты)
 * knightTrajectory(Square(7, 7), Square(8, 8)) =
 *     listOf(Square(7, 7), Square(5, 8), Square(4, 6), Square(6, 7), Square(8, 8))
 *
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun knightTrajectory(start: Square, end: Square): List<Square> {
    if (start == end) return listOf(start)
    val chessField = mutableMapOf<Square, Square>()
    for (i in 1..8) {
        for (j in 1..8) {
            chessField[Square(i, j)] = Square(-1, -1)
        }
    }
    val moves = knightMoveNumber(start, end)
    var temp = mutableListOf(start)
    for (l in 1..moves) {
        val nextTempMoves = mutableListOf<Square>()
        for (j in 0 until temp.size) {
            val nextMoves = listOf(
                Square(temp[j].column + 2, temp[j].row + 1),
                Square(temp[j].column + 2, temp[j].row - 1),
                Square(temp[j].column - 2, temp[j].row + 1),
                Square(temp[j].column - 2, temp[j].row - 1),
                Square(temp[j].column + 1, temp[j].row + 2),
                Square(temp[j].column + 1, temp[j].row - 2),
                Square(temp[j].column - 1, temp[j].row + 2),
                Square(temp[j].column - 1, temp[j].row - 2)
            ).filter { it.inside() }
            nextTempMoves += nextMoves
            for (nextMove in nextMoves) {
                if (chessField[nextMove] == Square(-1, -1))
                    chessField[nextMove] = temp[j]
            }
        }
        temp = nextTempMoves
    }
    val ans = mutableListOf<Square>()
    var res = end
    for (k in 1..moves) {
        val move = chessField[res]!!
        ans += move
        res = move
    }
    return ans.reversed() + end
}

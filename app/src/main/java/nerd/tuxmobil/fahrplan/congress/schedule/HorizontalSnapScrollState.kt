package nerd.tuxmobil.fahrplan.congress.schedule

import androidx.annotation.VisibleForTesting
import info.metadude.android.eventfahrplan.commons.logging.Logging
import java.lang.Integer.min
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Immutable state for [HorizontalSnapScrollView].
 * Create a copy to mutate values.
 *
 * Acts as a delegate for mathematical calculations for [HorizontalSnapScrollView]
 * so that these can be unit tested easily.
 */
data class HorizontalSnapScrollState @JvmOverloads constructor(

    val logging: Logging,
    val xStart: Int = 0,
    val displayColumnCount: Int = DEFAULT_DISPLAY_COLUMNS_COUNT,
    val roomsCount: Int = NOT_INITIALIZED,
    val columnWidth: Int = DEFAULT_COLUMNS_WIDTH,
    var activeColumnIndex: Int = DEFAULT_ACTIVE_COLUMN_INDEX

) {

    companion object {
        private const val LOG_TAG = "HorizontalSnapScrollState"
        private const val NOT_INITIALIZED = Int.MIN_VALUE
        private const val DEFAULT_DISPLAY_COLUMNS_COUNT = 1
        private const val DEFAULT_COLUMNS_WIDTH = 0
        private const val DEFAULT_ACTIVE_COLUMN_INDEX = 0

        @VisibleForTesting
        const val SCROLL_THRESHOLD_FACTOR = 0.25
    }

    init {
        check(xStart >= 0) { "xStart cannot be less then 0 but is $xStart." }
        check(displayColumnCount > 0) { "displayColumnCount cannot be 0." }
        check(columnWidth >= 0) { "columnWidth cannot be less then 0 but is $columnWidth." }
        if (activeColumnIndex < 0) {
            activeColumnIndex = DEFAULT_ACTIVE_COLUMN_INDEX
        }
    }

    fun isRoomsCountInitialized() = roomsCount != NOT_INITIALIZED

    @VisibleForTesting
    val maxColumnIndex
        get() = max(roomsCount - displayColumnCount, 0)

    /**
     * Returns the column index calculated based on the given [scrollX] value
     * and the current state of [HorizontalSnapScrollState].
     */
    fun calculateOnTouchColumnIndex(scrollX: Int): Int {
        val distance = scrollX - xStart
        logging.d(
            LOG_TAG,
            """column width: $columnWidth, scrollX: $scrollX, distance: $distance, activeColumnIndex: $activeColumnIndex"""
        )
        var columnIndex = activeColumnIndex
        if (displayColumnCount > 1) {
            val columnIndexDelta = (abs(distance.toFloat()) / columnWidth).roundToInt()
            logging.d(LOG_TAG, "column distance: $columnIndexDelta")
            columnIndex = if (distance > 0) {
                activeColumnIndex - columnIndexDelta
            } else {
                activeColumnIndex + columnIndexDelta
            }
        } else {
            if (abs(distance) > columnWidth * SCROLL_THRESHOLD_FACTOR) {
                columnIndex = if (distance > 0) {
                    activeColumnIndex - 1
                } else {
                    activeColumnIndex + 1
                }
            }
        }

        columnIndex = max(columnIndex, 0)
        columnIndex = min(columnIndex, roomsCount - displayColumnCount)

        return columnIndex
    }

    /**
     * Returns the horizontal position calculated based on the given [columnIndex]
     * and the current values of [maxColumnIndex] and [columnWidth].
     */
    fun calculateScrollToXPosition(columnIndex: Int): Int {
        val constraintColumnIndex = when {
            columnIndex < 0 -> 0
            columnIndex > maxColumnIndex -> maxColumnIndex
            else -> columnIndex
        }
        return constraintColumnIndex * columnWidth
    }

    /**
     * Returns the column width calculated based on the given [measuredWidth]
     * and the current value of [displayColumnCount].
     */
    fun calculateColumnWidth(measuredWidth: Int): Int {
        return (measuredWidth.toFloat() / displayColumnCount).roundToInt()
    }

}

package cz.vutbr.fit.meetmeal.utils

import androidx.databinding.*

object Converter {
  @JvmStatic
  @InverseMethod("stringToInt")
  fun intToString(oldValue: Int, value: Int): String? {
    return if (value == 0) {
      null
    } else value.toString()
  }

  @JvmStatic
  fun stringToInt(oldValue: Int, value: String?): Int {
    if (value == null || value.isEmpty()) {
      return 0
    }
    try {
      return Integer.parseInt(value)
    } catch (e: NumberFormatException) {
      return oldValue
    }
  }
}
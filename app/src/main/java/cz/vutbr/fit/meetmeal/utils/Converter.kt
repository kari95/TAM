package cz.vutbr.fit.meetmeal.utils

import android.view.*
import android.widget.*
import androidx.databinding.*
import android.widget.TextView
import androidx.databinding.InverseMethod
import java.text.*

object Converter {
  @JvmStatic
  @InverseMethod("stringToInt")
  fun intToString(
    value: Int?
  ): String {
    // Converts long to String.
    if (value != null) {
      return value.toString()
    }
    else {
      return ""
    }

  }

  @JvmStatic
  fun stringToInt(
    value: String
  ): Int? {
    // Converts String to long.
    if ((value != "") || (value != null)) {
      return value.toIntOrNull()
    }
    else return null
  }

  @JvmStatic
  @InverseMethod("toDouble")
  fun toString(
    value: Double): String {
    return value.toString()
  }

  @JvmStatic
  fun toDouble(value: String): Double {
    return value.toDouble()
  }
}
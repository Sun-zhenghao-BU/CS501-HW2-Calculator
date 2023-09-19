package com.example.ec501_hw2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ec501_hw2.databinding.ActivityCalculatorBinding
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.lang.Math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val b1:Button   = findViewById(R.id.button_1)
        val b2:Button   = findViewById(R.id.button_2)
        val b3:Button   = findViewById(R.id.button_3)
        val b4:Button   = findViewById(R.id.button_4)
        val b5:Button   = findViewById(R.id.button_5)
        val b6:Button   = findViewById(R.id.button_6)
        val b7:Button   = findViewById(R.id.button_7)
        val b8:Button   = findViewById(R.id.button_8)
        val b9:Button   = findViewById(R.id.button_9)
        val b0:Button   = findViewById(R.id.button_0)
        val b_add:Button   = findViewById(R.id.button_add)
        val b_minus:Button   = findViewById(R.id.button_minus)
        val b_mul:Button   = findViewById(R.id.button_mul)
        val b_div:Button   = findViewById(R.id.button_div)
        val b_sqrt:Button   = findViewById(R.id.button_sqrt)
        val b_equal:Button   = findViewById(R.id.button_equal)
        val text_ans:EditText = findViewById(R.id.show_ans)
        val b_clear:Button = findViewById(R.id.button_clear)
        val b_dot:Button = findViewById(R.id.button_dot)


        var lastIsNum = true
        var operatingMul = false
        var operatingDiv = false
        var nums=mutableListOf<Double>()
        var isFraction = false
        var FractionLocation = 0
        var lastOperator = ""
        var divZero = false

        fun ifOutputFraction():Boolean{
            if(nums.size==2) return !(nums[0]==nums[0].toInt().toDouble() && nums[1]==nums[1].toInt().toDouble())
            return if(nums.size==1) nums[0] != nums[0].toInt().toDouble()
            else true
        }

        fun num_fun(num:Int){
            if (lastIsNum || operatingMul || operatingDiv){
                var temp=0.0
                if (!nums.isEmpty()) {
                    temp = nums.removeAt(nums.size - 1)
                }

                if (!isFraction){
                    if(temp < 0){
                        temp=temp*10-num
                    }else{
                        temp=temp*10+num
                    }
                    nums.add(temp)
                    text_ans.setText(temp.toInt().toString())
                }
                else {
                    FractionLocation+=1
                    if(temp < 0){
                        temp=temp-num*Math.pow(0.1, FractionLocation.toDouble())
                    }else{
                        temp=temp+num*Math.pow(0.1, FractionLocation.toDouble())
                    }
                    nums.add(temp)
                    text_ans.setText(temp.toString())
                }
            } else {
                when (lastOperator) {
                    "add" -> {
                        if(nums.size==2){
                            nums[0]=text_ans.text.toString().toDouble()
                            nums[1]=num.toDouble()
                            text_ans.setText(num.toString())
                        } else {
                            nums.add(num.toDouble())
                            text_ans.setText(num.toString())
                        }
                    }
                    "minus" -> {
                        if(nums.size==2){
                            nums[0]=text_ans.text.toString().toDouble()
                            nums[1]=(-num).toDouble()
                            text_ans.setText((-num).toString())
                        } else {
                            nums.add((-num).toDouble())
                            text_ans.setText((-num).toString())
                        }
                    }
                    "mul" -> {
                        operatingMul=true
                        nums.add(num.toDouble())
                        text_ans.setText(num.toString())
                    }
                    "div" -> {
                        operatingDiv=true
                        nums.add(num.toDouble())
                        text_ans.setText(num.toString())
                    }
                }
            }
            lastIsNum=true
        }

        fun divide_zero(){
            text_ans.setText("error")
            lastIsNum = true
            nums=mutableListOf<Double>()
            isFraction = false
            FractionLocation = 0
            lastOperator=""
            divZero=false
        }

        b_mul.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else if(!nums.isEmpty()){
                    lastIsNum=false
                    lastOperator="mul"
                }
            }
        } as View.OnClickListener)

        b_div.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else if(!nums.isEmpty()){
                    lastIsNum=false
                    lastOperator="div"
                }
            }
        } as View.OnClickListener)

        b_equal.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    isFraction = false
                    FractionLocation = 0
                    lastIsNum = true
                    lastOperator = ""
                    if(operatingMul){
                        operatingMul=false
                        if(nums.size==2){
                            nums[0]=nums[0]*nums[1]
                            nums.removeAt(nums.size - 1)
                        }
                        if(nums.size==3){
                            nums[1]=nums[1]*nums[2]
                            nums.removeAt(nums.size - 1)
                            nums[0]=nums[0]+nums[1]
                            nums.removeAt(nums.size - 1)
                        }
                        if(nums[0]==nums[0].toInt().toDouble()){
                            text_ans.setText(nums[0].toInt().toString())
                        }else{
                            text_ans.setText(nums[0].toString())
                            isFraction=true
                            FractionLocation=countDecimalPlaces(nums[0].toString())
                        }
                    } else if (operatingDiv){
                        operatingDiv=false
                        if(nums[nums.size - 1]==0.0){
                            text_ans.setText("error")
                            lastIsNum = true
                            nums=mutableListOf<Double>()
                            lastOperator=""
                            divZero=false
                            operatingMul=false
                        } else {
                            if (nums.size == 2) {

                                nums[0] = nums[0] / nums[1]
                                nums.removeAt(nums.size - 1)
                            }
                            if (nums.size == 3) {
                                nums[1] = nums[1] / nums[2]
                                nums.removeAt(nums.size - 1)
                                nums[0] = nums[0] + nums[1]
                                nums.removeAt(nums.size - 1)
                            }
                            if (nums[0] == nums[0].toInt().toDouble()) {
                                text_ans.setText(nums[0].toInt().toString())
                            } else {
                                text_ans.setText(nums[0].toString())
                                isFraction=true
                                FractionLocation=countDecimalPlaces(nums[0].toString())
                            }
                        }
                    } else {
                        if (nums.size == 2) {
                            if (nums[0] + nums[1] == (nums[0] + nums[1]).toInt().toDouble()) {
                                text_ans.setText((nums[0] + nums[1]).toInt().toString())
                                nums[0] = text_ans.text.toString().toDouble()
                                nums.removeAt(nums.size - 1)
                            } else {
                                text_ans.setText((nums[0] + nums[1]).toString())
                                nums[0] = text_ans.text.toString().toDouble()
                                nums.removeAt(nums.size - 1)
                                isFraction=true
                                FractionLocation=countDecimalPlaces(nums[0].toString())
                            }
                        }
                    }
                }
            }
        } as View.OnClickListener)

        b_add.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    isFraction = false
                    FractionLocation = 0
                    lastIsNum = false
                    lastOperator = "add"
                    if (nums.size == 2) {
                        if (nums[0] + nums[1] == (nums[0] + nums[1]).toInt().toDouble()) {
                            text_ans.setText((nums[0] + nums[1]).toInt().toString())
                        } else {
                            text_ans.setText((nums[0] + nums[1]).toString())
                        }
                    }
                }
            }
        } as View.OnClickListener)

        b_minus.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    isFraction = false
                    FractionLocation = 0
                    lastIsNum = false
                    lastOperator = "minus"
                    if (nums.size == 2) {
                        if (nums[0] + nums[1] == (nums[0] + nums[1]).toInt().toDouble()) {
                            text_ans.setText((nums[0] + nums[1]).toInt().toString())
                        } else {
                            text_ans.setText((nums[0] + nums[1]).toString())
                        }
                    }
                }
            }
        } as View.OnClickListener)

        b_dot.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                isFraction=true
                divZero=false
            }
        } as View.OnClickListener)

        b_clear.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                lastIsNum = true
                nums=mutableListOf<Double>()
                text_ans.setText("0")
                isFraction = false
                FractionLocation = 0
                lastOperator=""
                divZero=false
                operatingMul=false
            }
        } as View.OnClickListener)


        b_sqrt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var temp=0.0
                if (!nums.isEmpty()) {
                    temp = nums.removeAt(nums.size - 1)
                }
                temp= sqrt(temp.toDouble())
                nums.add(temp)
                text_ans.setText(temp.toString())
                Toast.makeText(this@MainActivity, "sqrt", Toast.LENGTH_SHORT).show()

            }
        } as View.OnClickListener)


        b1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(1)
                    Toast.makeText(this@MainActivity, "1", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(2)
                    Toast.makeText(this@MainActivity, "2", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(3)
                    Toast.makeText(this@MainActivity, "3", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(4)
                    Toast.makeText(this@MainActivity, "4", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b5.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(5)
                    Toast.makeText(this@MainActivity, "5", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b6.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(6)
                    Toast.makeText(this@MainActivity, "6", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b7.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(7)
                    Toast.makeText(this@MainActivity, "7", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b8.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(8)
                    Toast.makeText(this@MainActivity, "8", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b9.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(9)
                    Toast.makeText(this@MainActivity, "9", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        b0.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(divZero) divide_zero()
                else {
                    num_fun(0)
                    Toast.makeText(this@MainActivity, "0", Toast.LENGTH_SHORT).show()
                    lastIsNum = true
                }
            }
        } as View.OnClickListener)

        text_ans.addTextChangedListener(object : TextWatcher {
            var before_change=""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                before_change=text_ans.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val validChars = "0123456789.-"
                val invalidChars = text_ans.text.toString().filter { it !in validChars }
                var temp=0.0
                if(nums.isNotEmpty()) temp = nums.removeAt(nums.size - 1)
                if (invalidChars.isEmpty() && text_ans.text.toString()!="") {
                    temp = text_ans.text.toString().toDouble()
                }
                nums.add(temp)
                if (invalidChars.isNotEmpty()){
                    lastIsNum = true
                    nums=mutableListOf<Double>()
                    text_ans.setText("error")
                    isFraction = false
                    FractionLocation = 0
                    lastOperator=""
                    divZero=false
                    operatingMul=false
                    Toast.makeText(this@MainActivity, "error input", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}


fun countDecimalPlaces(input: String): Int {
    val decimalIndex = input.indexOf('.')
    if (decimalIndex == -1 || decimalIndex == input.length - 1) {
        return 0
    }
    val decimalPart = input.substring(decimalIndex + 1)
    val trimmedDecimalPart = decimalPart.replace("0*$".toRegex(), "")
    return trimmedDecimalPart.length
}
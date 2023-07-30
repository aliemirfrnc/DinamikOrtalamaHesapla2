package com.example.dinamikortalamahesapla2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private val DERSLER = arrayOf("Matematik", "Türkçe", "Tarih", "Kimya", "Biyoloji", "Coğrafya")
    private var tumDerslerinBilgileri = ArrayList<Dersler>()


    lateinit var autoTextViewDersAd: AutoCompleteTextView
    lateinit var spnDersKredi: Spinner
    lateinit var spnDersNot: Spinner
    lateinit var btnEkle: Button
    lateinit var btnHesapla: Button
    lateinit var rootLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        autoTextViewDersAd = findViewById(R.id.autoTextViewDersAd)
        spnDersKredi = findViewById(R.id.spnDersKredi)
        spnDersNot = findViewById(R.id.spnDersNot)
        btnEkle = findViewById(R.id.btnEkle)
        rootLayout = findViewById(R.id.rootLayout)
        btnHesapla = findViewById(R.id.btnHesapla)

        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, DERSLER)
        autoTextViewDersAd.setAdapter(adapter)



        if (rootLayout.childCount == 0) {
            btnHesapla.visibility = View.INVISIBLE
        } else {
            btnHesapla.visibility = View.VISIBLE
        }

        btnEkle.setOnClickListener {

            if (!autoTextViewDersAd.text.isNullOrEmpty()) {


                var inflater = LayoutInflater.from(this)
                var yeniDersView = inflater.inflate(R.layout.yeni_ders_layout, null)


                val autoTextViewDersAdYeni =
                    yeniDersView.findViewById<AutoCompleteTextView>(R.id.autoTextViewDersAdYeni)
                val spnYeniDersKredi = yeniDersView.findViewById<Spinner>(R.id.spnYeniDersKredi)
                val spnYeniDersNot = yeniDersView.findViewById<Spinner>(R.id.spnYeniDersNot)
                val btnSil = yeniDersView.findViewById<Button>(R.id.btnSil)

                autoTextViewDersAdYeni.setAdapter(adapter)


                var dersAdi = autoTextViewDersAd.text.toString()
                var dersKredi = spnDersKredi.selectedItem.toString()
                var dersHarf = spnDersNot.selectedItem.toString()



                autoTextViewDersAdYeni.setText(dersAdi)
                spnYeniDersKredi.setSelection(spinnerDegeriIndexiniBul(spnDersKredi, dersKredi))
                spnYeniDersNot.setSelection(spinnerDegeriIndexiniBul(spnDersNot, dersHarf))

                btnSil.setOnClickListener {
                    rootLayout.removeView(yeniDersView)

                    if (rootLayout.childCount == 0) {
                        btnHesapla.visibility = View.INVISIBLE
                    } else {
                        btnHesapla.visibility = View.VISIBLE
                    }
                }
                rootLayout.addView(yeniDersView)

                btnHesapla.visibility = View.VISIBLE

                sifirla()
            } else {
                Toast.makeText(this, "Lütfen Ders Adını Giriniz.", Toast.LENGTH_SHORT).show()
            }
        }

        btnHesapla.setOnClickListener {
            var toplamNot: Double = 0.0
            var toplamKredi: Double = 0.0
            for (i in 0 until rootLayout.childCount) {
                val tekSatir = rootLayout.getChildAt(i)
                val autoTextViewDersAdYeni = tekSatir.findViewById<AutoCompleteTextView>(R.id.autoTextViewDersAdYeni)
                val spnYeniDersKredi = tekSatir.findViewById<Spinner>(R.id.spnYeniDersKredi)
                val spnYeniDersNot = tekSatir.findViewById<Spinner>(R.id.spnYeniDersNot)

                val geciciDers = Dersler(
                    autoTextViewDersAdYeni.text.toString(),
                    (spnYeniDersKredi.selectedItemPosition + 1).toString(),
                    spnYeniDersNot.selectedItem.toString()
                )
                tumDerslerinBilgileri.add(geciciDers)
            }

            for (oankiDers in tumDerslerinBilgileri) {
                toplamNot += harfiNotaCevir(oankiDers.dersHarfNot) * oankiDers.dersKredi.toDouble()
                toplamKredi += oankiDers.dersKredi.toDouble()
            }

            if (toplamKredi > 0.0) {
                val ortalama = toplamNot / toplamKredi
                Toast.makeText(this, "Ortalama: $ortalama", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Derslerin kredisi sıfır olamaz!", Toast.LENGTH_LONG).show()
            }

            tumDerslerinBilgileri.clear()
        }
    }

    fun sifirla() {
        autoTextViewDersAd.setText("")
        spnDersKredi.setSelection(0)
        spnDersNot.setSelection(0)
    }

    fun spinnerDegeriIndexiniBul(spinner: Spinner, aranacakDeger: String): Int {
        var index = 0
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == aranacakDeger) {
                index = i
                break
            }
        }
        return index
    }

    fun harfiNotaCevir(gelenNotHarfDegeri: String): Double {
        var deger = 0.0
        when(gelenNotHarfDegeri) {
            "AA" -> deger = 4.0
            "BA" -> deger = 3.5
            "BB" -> deger = 3.0
            "BC" -> deger = 2.5
            "CC" -> deger = 2.0
            "DC" -> deger = 1.5
            "DD" -> deger = 1.0
            "FF" -> deger = 0.0
        }
        return deger

    }
}





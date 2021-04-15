package com.mobiquel.lehpermitscanner.service

/**
 * Created by Arman Reyaz on 4/3/2021.
 */

data class Circuit(var id:String,var name:String)

object Supplier{
    val domesticCkts= listOf<Circuit>(
        Circuit("1","Khalsi,Domkhar, Skurbuchan, Hanu, Baima, Dha in Khalsi Sub-Division."),
        Circuit("2","Khardong, Khalsar, Trith upto  Warsi and Yarma Gonbo, Disket, Hunder, Turtuk, Pachathang, Tyakshi in Nubra Sub-Division."),
        Circuit("3","Upshi, TsomoRiRi/Korzok in Nyoma Sub-Division."),
        Circuit("4","Upshi, Dipling, Puga, TsomoRiRi/Korzok in Nyoma Sub-Division."),
        Circuit("5","Kharu, Changla, Durbuk, Tangtse, Lukhung."),
        Circuit("6","Upshi, Chumathang, Mahe, Loma Bend in Nyoma Division."),
        Circuit("7","Leh-Kharu-Changla-Durbuk-Tangtse-Lukung.")
    )

    val ForeignCkts= listOf<Circuit>(
        Circuit("1","Khalsi,Domkhar, Skurbuchan, Hanu, Baima, Dha in Khalsi Sub-Division."),
        Circuit("2","Khardong, Khalsar, Trith upto Warsi and Yarma Gonbo, Disket, Hunder, Turtuk, Pachathang, Tyakshi in Nubra Sub-Division."),
        Circuit("3","TsomoRiRi/Korzok in Nyoma Sub-Division."),
        Circuit("4","Upshi, Dipling, Puga, TsomoRiRi/Korzok in Nyoma Sub-Division."),
        Circuit("5","Kharu, Changla, Durbuk, Tangtse, Lukhung, Spangmik, Man-merag(Pangong Lake) in Nyoma Sub-Division."),
        Circuit("6","Upshi, Chumathang, Mahe, Loma Bend in Nyoma Division."),
        Circuit("7","Chushul- Kartsangla- Mahe"),
        Circuit("8","Durbuk- Shachukul- Tharuk- Sato Kargyam- Parma- Erath- Chushul and Loma- Hanley (Not Allowed to Umling Pass)"),
        Circuit("9","Spankmik to Man and Merak in the Pangong Lake area and from Mahe to Loma Bend and from Merak to Loma Bend."),
        Circuit("10","Korzok- Nurb- Sumdo- Parangla- Kazaand"),
        Circuit("11","Agyam- Shayok- Durbuk"),
        Circuit("12","Phyang- Dokla- HunderDok- Hunder (For Trekking)"),
        Circuit("13","Basgo- Ney- HunderDok- Hunder (For Trekking)"),
        Circuit("14","Temisgam- Largyap- Pachnathang- Skuru (For Trekking)"),
        Circuit("15","Saspol- Saspochey- Rakurla- Skuru (For Trekking)")
    )






}
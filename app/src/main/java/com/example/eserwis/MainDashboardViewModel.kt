package com.example.eserwis

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


data class Fault(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val department: String,
    val priority: String, //krytyczna, normalna, pomniejsza
    val status: String, //zgloszone, w trakcie pracy, zakonczone
    val assignedTo: String? = null //UID technika
)

class MainDashboardViewModel : ViewModel() {

    //symulacja usterek
    //TODO: pobieranie usterek z firebase
    private val userUid = "uid_technik_1" // symulowane UID
    private val managerDepartment = "Hala A" // symulowany wydzial

    private val _allFaults = mutableStateOf(
        listOf(
            Fault("F001", "Awaria pompy","Pompa przestała działać", "Hala A, P1", "Hala A", "krytyczna", "zgłoszone", null),
            Fault("F002", "Drobny wyciek","Wyciek wody", "Sekcja B, T5", "Hala A", "normalna", "zgłoszone", "uid_technik_1"),
            Fault("F003", "Problem z HMI","Panel HMI nie reaguje", "Hala A, C3", "Hala A", "wysoki", "zgłoszone", null),
            Fault("F004", "Konserwacja","Konserwacja urządzenia", "Sekcja B, M4", "Sekcja B", "zakończone", "zakończone", "uid_technik_2")
        )
    )
    val allFaults: State<List<Fault>> = _allFaults

}
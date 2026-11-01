package com.dcsantiago567.kotlinpetmanager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dcsantiago567.kotlinpetmanager.model.Paciente

class PacienteViewModel : ViewModel() {

    // Lista de pacientes: la vista la lee, pero solo el ViewModel la modifica
    var pacientes by mutableStateOf<List<Paciente>>(emptyList())
        private set

    // Estado de los campos del formulario
    var nombre by mutableStateOf("")
        private set
    var especie by mutableStateOf("")
        private set
    var edad by mutableStateOf("")
        private set
    var contactoEmergencia by mutableStateOf("")
        private set
    var esUrgencia by mutableStateOf(false)
        private set

    // Mensaje de validación que la vista puede mostrar (null = sin error)
    var mensajeError by mutableStateOf<String?>(null)
        private set

    private var siguienteId = 1

    // Eventos que la vista le envía al ViewModel
    fun onNombreChange(valor: String) { nombre = valor }
    fun onEspecieChange(valor: String) { especie = valor }
    fun onEdadChange(valor: String) { edad = valor.filter { it.isDigit() }.take(2) }
    fun onContactoChange(valor: String) { contactoEmergencia = valor }
    fun onUrgenciaChange(valor: Boolean) { esUrgencia = valor }

    fun agregarPaciente() {
        val edadNumero = edad.toIntOrNull()

        when {
            nombre.isBlank() -> {
                mensajeError = "El nombre es obligatorio"
                return
            }
            especie.isBlank() -> {
                mensajeError = "La especie o raza es obligatoria"
                return
            }
            edadNumero == null || edadNumero !in 0..30 -> {
                mensajeError = "La edad debe ser un número entre 0 y 30"
                return
            }
        }

        val nuevo = Paciente(
            id = siguienteId++,
            nombre = nombre.trim(),
            especie = especie.trim(),
            edad = edadNumero!!,
            esUrgencia = esUrgencia,
            contactoEmergencia = contactoEmergencia.trim().ifBlank { null }
        )

        pacientes = pacientes + nuevo
        limpiarFormulario()
    }

    fun limpiarFormulario() {
        nombre = ""
        especie = ""
        edad = ""
        contactoEmergencia = ""
        esUrgencia = false
        mensajeError = null
    }
}
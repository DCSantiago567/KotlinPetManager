package com.dcsantiago567.kotlinpetmanager.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dcsantiago567.kotlinpetmanager.model.Paciente
import com.dcsantiago567.kotlinpetmanager.viewmodel.PacienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteScreen(viewModel: PacienteViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KotlinPet Manager") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            FormularioPaciente(viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pacientes registrados (${viewModel.pacientes.size})",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            ListaPacientes(
                pacientes = viewModel.pacientes,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun FormularioPaciente(viewModel: PacienteViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        OutlinedTextField(
            value = viewModel.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.especie,
            onValueChange = viewModel::onEspecieChange,
            label = { Text("Especie / Raza") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = viewModel.edad,
                onValueChange = viewModel::onEdadChange,
                label = { Text("Edad") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("Urgencia")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = viewModel.esUrgencia,
                onCheckedChange = viewModel::onUrgenciaChange
            )
        }

        OutlinedTextField(
            value = viewModel.contactoEmergencia,
            onValueChange = viewModel::onContactoChange,
            label = { Text("Contacto de emergencia") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        viewModel.mensajeError?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = viewModel::agregarPaciente,
                modifier = Modifier.weight(1f)
            ) {
                Text("Registrar Paciente")
            }
            OutlinedButton(onClick = viewModel::limpiarFormulario) {
                Text("Limpiar")
            }
        }
    }
}

@Composable
fun ListaPacientes(pacientes: List<Paciente>, modifier: Modifier = Modifier) {
    if (pacientes.isEmpty()) {
        Text(
            text = "Aún no hay pacientes registrados.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pacientes, key = { it.id }) { paciente ->
                TarjetaPaciente(paciente)
            }
        }
    }
}

@Composable
fun TarjetaPaciente(paciente: Paciente) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (paciente.esUrgencia)
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = paciente.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                if (paciente.esUrgencia) {
                    Text(
                        text = "URGENTE",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            val unidad = if (paciente.edad == 1) "año" else "años"
            Text(
                text = "${paciente.especie} • ${paciente.edad} $unidad",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Contacto: ${paciente.contactoEmergencia ?: "Sin contacto registrado"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

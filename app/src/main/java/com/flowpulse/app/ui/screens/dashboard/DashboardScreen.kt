package com.flowpulse.app.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowpulse.app.R
import com.flowpulse.app.domain.model.InstanceMetrics
import com.flowpulse.app.domain.model.MetricsPoint
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.ui.viewinterop.AndroidView
import java.text.SimpleDateFormat
import java.util.Locale
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.Date

@Composable
fun DashboardScreen(
    instanceId: Long,
    onViewWorkflows: () -> Unit,
    onViewExecutions: () -> Unit,
    onViewAlerts: () -> Unit,
    onViewActions: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(instanceId) { viewModel.load(instanceId) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopAppBar(title = { Text(text = stringResource(id = R.string.dashboard_title)) })
            Spacer(modifier = Modifier.height(16.dp))
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(text = state.error ?: "", color = MaterialTheme.colorScheme.error)
                state.metrics != null -> DashboardContent(
                    metrics = state.metrics!!,
                    onViewWorkflows = onViewWorkflows,
                    onViewExecutions = onViewExecutions,
                    onViewAlerts = onViewAlerts,
                    onViewActions = onViewActions
                )
            }
        }
    }
}

@Composable
private fun DashboardContent(
    metrics: InstanceMetrics,
    onViewWorkflows: () -> Unit,
    onViewExecutions: () -> Unit,
    onViewAlerts: () -> Unit,
    onViewActions: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DashboardStat(title = stringResource(id = R.string.dashboard_active_workflows), value = metrics.activeWorkflows.toString())
            DashboardStat(title = stringResource(id = R.string.dashboard_executions_today), value = metrics.executionsToday.toString())
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DashboardStat(title = stringResource(id = R.string.dashboard_errors_today), value = metrics.errorsToday.toString())
            DashboardStat(title = stringResource(id = R.string.dashboard_latency), value = stringResource(id = R.string.latency_ms, metrics.averageLatencyMs))
        }
        if (metrics.chartPoints.isNotEmpty()) {
            ChartView(points = metrics.chartPoints)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onViewWorkflows, modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.workflows_title))
            }
            Button(onClick = onViewExecutions, modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.executions_title))
            }
            Button(onClick = onViewAlerts, modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.alerts_title))
            }
            Button(onClick = onViewActions, modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.actions_title))
            }
        }
    }
}

@Composable
private fun DashboardStat(title: String, value: String) {
    Column(modifier = Modifier.weight(1f)) {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun ChartView(points: List<MetricsPoint>) {
    val formatter = SimpleDateFormat("dd/MM", Locale.getDefault())
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                setTouchEnabled(false)
            }
        },
        update = { chart ->
            val entries = points.mapIndexed { index, point -> Entry(index.toFloat(), point.value.toFloat()) }
            val dataSet = LineDataSet(entries, "Ejecuciones").apply {
                color = android.graphics.Color.parseColor("#00A884")
                setDrawFilled(true)
                fillColor = android.graphics.Color.parseColor("#AA00A884")
                valueTextSize = 12f
            }
            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val idx = value.toInt().coerceIn(0, points.lastIndex)
                    return formatter.format(Date(points[idx].timestamp.toEpochMilliseconds()))
                }
            }
            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}

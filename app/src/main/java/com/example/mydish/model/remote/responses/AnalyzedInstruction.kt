package com.example.mydish.model.remote.responses

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)
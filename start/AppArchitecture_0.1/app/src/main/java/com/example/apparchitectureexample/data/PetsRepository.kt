package com.example.apparchitectureexample.data


interface PetsRepository {
    fun getPets(): List<Pet>
}
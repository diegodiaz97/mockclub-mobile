package com.diego.futty.core.presentation.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

object Transitions {

    private const val TIME_DURATION = 400

    // Left screen
    val LeftScreenEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val LeftScreenExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val LeftScreenPopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val LeftScreenPopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    // Right screen

    val RightScreenEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val RightScreenExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val RightScreenPopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val RightScreenPopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    // Bottom screen
    val BottomScreenEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Up,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val BottomScreenExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Down,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val BottomScreenPopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Up,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val BottomScreenPopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Down,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }
}

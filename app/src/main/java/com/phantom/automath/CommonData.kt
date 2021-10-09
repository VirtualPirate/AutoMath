package com.phantom.automath

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList

data class CommonData(
	val context: Context,
	val algebraDatabase: AlgebraDatabaseHandler,
	val drawerList: SnapshotStateList<Algebra>
	)

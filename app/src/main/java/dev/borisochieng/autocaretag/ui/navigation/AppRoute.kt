package dev.borisochieng.autocaretag.ui.navigation

import android.nfc.Tag
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.borisochieng.autocaretag.nfc_reader.ui.ClientDetailsScreen
import dev.borisochieng.autocaretag.nfc_reader.ui.NFCReaderViewModel
import dev.borisochieng.autocaretag.nfc_writer.presentation.viewModel.AddInfoViewModel
import dev.borisochieng.autocaretag.ui.screens.ReadStatusScreen
import dev.borisochieng.autocaretag.ui.manage.ClientScreen
import dev.borisochieng.autocaretag.ui.manage.ClientScreenViewModel
import dev.borisochieng.autocaretag.ui.screens.AddScreen
import dev.borisochieng.autocaretag.ui.screens.HomeScreen
import dev.borisochieng.autocaretag.ui.screens.MoreScreen
import dev.borisochieng.autocaretag.ui.screens.WriteStatusScreen
import dev.borisochieng.autocaretag.utils.animatedComposable
import org.koin.androidx.compose.koinViewModel

typealias ShouldScan = Boolean

@Composable
fun AppRoute(
    navActions: NavActions,
    navController: NavHostController,
    paddingValues: PaddingValues,
    scanNfc: (ShouldScan) -> Unit,
    addInfoViewModel: AddInfoViewModel = koinViewModel(),
    nfcReaderViewModel: NFCReaderViewModel = koinViewModel(),
    clientScreenViewModel: ClientScreenViewModel = koinViewModel(),
    tag: Tag? = null,
    setupNfc: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.HomeScreen.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(AppRoute.HomeScreen.route) {
            HomeScreen(
                viewModel = nfcReaderViewModel,
                scanForNFCTag = { scanNfc(true) },
                navigate = navActions::navigate
            )
        }
        animatedComposable(AppRoute.ReadStatusScreen.route) {
            ReadStatusScreen(
                viewModel = nfcReaderViewModel,
                navigate = navActions::navigate
            )
        }
        animatedComposable(AppRoute.AddScreen.route) {
            AddScreen(
                viewModel = addInfoViewModel,
                tag = tag,
                setupNfc = setupNfc,
                navigate = navActions::navigate
            )
        }
        animatedComposable(AppRoute.WriteStatusScreen.route) {
            WriteStatusScreen(
                viewModel = addInfoViewModel,
                navigate = navActions::navigate
            )
        }
        composable(AppRoute.MoreScreen.route) { MoreScreen() }
        composable(AppRoute.ManageScreen.route) {
            ClientScreen(
                viewModel = clientScreenViewModel,
                navigate = navActions::navigate
            )
        }
        animatedComposable(AppRoute.ClientDetailsScreen.route) { backstackEntry ->
            val clientId = backstackEntry.arguments?.getString("clientId") ?: ""
            LaunchedEffect(Unit) {
                nfcReaderViewModel.fetchClientDetails(clientId.toLong())
            }

            ClientDetailsScreen(
                viewModel = nfcReaderViewModel,
                updateClientInfo = nfcReaderViewModel::updateClientDetails,
                navigate = navActions::navigate
            )
        }
    }
}

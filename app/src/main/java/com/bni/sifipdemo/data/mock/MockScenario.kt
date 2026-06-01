package com.bni.sifipdemo.data.mock

/**
 * Scenario drives the deterministic responses of the SIFIP mock service.
 *
 * Switchable at runtime from the login screen (dropdown in the header) or
 * at build time via:
 * `./gradlew assembleDebug -PsifipScenario=FAIL_SIM_SWAP`
 */
enum class MockScenario(val label: String) {
    /**
     * Login checks OK. Fraud score depends on the transfer amount:
     *  - ≤ 1 000 000 FCFA → score 12 %, virement autorisé
     *  - >  1 000 000 FCFA → score 76 %, virement bloqué
     *
     * Scénario par défaut : la banque démontre les deux issues en variant
     * simplement le montant saisi.
     */
    ALL_OK("Par défaut (score selon montant, seuil 1 000 000 FCFA)"),

    /** Number-verify fails (e.g. user typed the wrong MSISDN). */
    FAIL_NUMBER_VERIFY("Échec Vérification réseau mobile"),

    /** Recent SIM swap detected → block login. */
    FAIL_SIM_SWAP("Échec Vérification ligne mobile (SIM changée)"),

    /** Unknown device → block login. */
    FAIL_DEVICE_SWAP("Échec Vérification Smartphone (appareil inconnu)"),

    /** Forces fraud REJECT regardless of amount — pour démo blocage explicite. */
    FAIL_FRAUD("Score fraude élevé forcé (transaction toujours bloquée)"),
}

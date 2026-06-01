# BNI Côte d'Ivoire × SIFIP — Android Demo

Application Android native (Kotlin + Jetpack Compose + Material 3) de **démonstration
bancaire** destinée à être présentée à la **BNI (Banque Nationale d'Investissement) —
Côte d'Ivoire — bni.ci**.

Elle simule un parcours bancaire sécurisé intégrant les APIs SIFIP **en mode mock** :

| Niveau | Étape                       | APIs SIFIP appelées (mock)                                                                |
| :----- | :-------------------------- | :---------------------------------------------------------------------------------------- |
| 1      | Authentification (Login)    | Vérification réseau mobile → Vérification ligne mobile → Vérification Smartphone → Autorisation |
| 2      | Transaction (Virement)      | Fraud Engine (score IA basé sur le montant + décision APPROVE/REJECT)                     |

Tournée sur n'importe quel device Android **8+ (API 26)** — pas de réseau requis.

---

## 1. Pré-requis

| Outil                | Version recommandée |
| :------------------- | :------------------ |
| Android Studio       | Hedgehog (2023.1.1) ou plus récent |
| JDK                  | 17                  |
| Android SDK Platform | 34                  |
| Gradle               | 8.9 (téléchargé automatiquement par le wrapper) |

## 2. Compiler et lancer

### Depuis Android Studio

1. **File → Open** → sélectionnez le dossier `bni-android-demo`.
2. Laissez Gradle finir la synchronisation (≈ 2 min la 1re fois).
3. Branchez l'appareil et sélectionnez-le dans la barre supérieure.
4. **Run ▶ app**.

### Depuis la ligne de commande

```powershell
# Windows
cd bni-android-demo
.\gradlew.bat installDebug
```

```bash
# Linux / macOS
cd bni-android-demo
./gradlew installDebug
```

> ⚠ Si le wrapper Gradle (`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`)
> n'est pas présent dans votre clone, générez-le une fois avec un Gradle local :
> `gradle wrapper --gradle-version 8.9`

## 3. Scénarios de démo

Le **mock SIFIP** est complètement isolé dans
`app/src/main/java/com/bni/sifipdemo/data/mock/`. Le scénario actif définit
les réponses des 4 APIs et est commutable de **trois façons** :

### 3.1 Au runtime (recommandé pour la démo live)

Dans le bandeau navy de l'écran de **Login**, un menu déroulant permet de basculer
instantanément vers :

| Scénario              | Effet                                                                                  |
| :-------------------- | :------------------------------------------------------------------------------------- |
| `ALL_OK` (défaut)     | Login ✅ + score fraude basé sur le montant : ≤ 1 000 000 FCFA → 12 % OK, > → 76 % bloqué |
| `FAIL_NUMBER_VERIFY`  | Vérification réseau mobile ❌ → login bloqué dès l'étape 1                              |
| `FAIL_SIM_SWAP`       | Vérification ligne mobile ❌ (changement SIM récent détecté)                            |
| `FAIL_DEVICE_SWAP`    | Vérification Smartphone ❌ (appareil inconnu)                                           |
| `FAIL_FRAUD`          | Login OK mais score fraude 87 % forcé → virement toujours bloqué                       |

### 3.2 À la compilation (CI / build automatisé)

```powershell
.\gradlew.bat assembleDebug -PsifipScenario=FAIL_FRAUD
```

Le scénario par défaut au démarrage est lu dans `BuildConfig.DEFAULT_SIFIP_SCENARIO`.

### 3.3 Par code (tests, démos scriptées)

```kotlin
val app = context.applicationContext as BniApplication
app.sifipMock.setScenario(MockScenario.FAIL_SIM_SWAP)
```

## 4. Architecture

```
app/
├── BniApplication.kt           ← composition root (1 endroit pour swapper le mock)
├── MainActivity.kt
├── ViewModelFactories.kt
├── navigation/
│   └── NavGraph.kt             ← Splash → Login → Dashboard → Transfer
├── data/
│   ├── model/                  ← DTOs SIFIP + modèles compte
│   ├── mock/
│   │   ├── SifipApi.kt          ← interface — contrat unique
│   │   ├── SifipMockService.kt  ← implémentation locale (à remplacer par Retrofit)
│   │   └── MockScenario.kt
│   └── repository/
│       └── BankRepository.kt    ← données mock du dashboard (FCFA, contexte ivoirien)
└── ui/
    ├── splash/                  ← Splash écran (logo BNI)
    ├── login/                   ← 4 SIFIP checks animés (3 vérif + autorisation)
    ├── dashboard/               ← Solde FCFA + transactions + bouton virement
    ├── transfer/                ← Formulaire + jauge fraud score IA
    ├── components/              ← BniButton, CheckStepRow, FraudGauge, BniLogo
    └── theme/                   ← Couleurs BNI navy/gold, Material 3
```

**Pattern** : MVVM + StateFlow (Compose). Chaque ViewModel expose un seul
`StateFlow<UiState>`, observé via `collectAsState()` côté Composable.

## 5. Identité visuelle BNI

| Couleur          | Hex        | Usage                          |
| :--------------- | :--------- | :----------------------------- |
| BNI Navy         | `#1A3668`  | Header, primary color          |
| BNI Navy Dark    | `#0E1F45`  | Gradient header / status bar   |
| BNI Gold         | `#F2B544`  | Accents, secondary color       |
| BNI Gold Dark    | `#C99524`  | Tertiary, badges               |

Le logo livré dans `res/drawable/bni_logo.xml` est un **placeholder
typographique générique** (B/N/I sur fond navy + accent doré). Pour
utiliser le logo officiel BNI, **remplacez ce fichier** par le SVG converti
via Android Studio (*New → Vector Asset*) ou par un PNG `bni_logo.png`
dans `res/drawable-xxhdpi/`. Aucune autre modification de code n'est requise.

## 6. Brancher la vraie API SIFIP

Le seul fichier à toucher est `BniApplication.kt` :

```kotlin
// avant (démo)
sifipMock = SifipMockService(initial)
val sifipApi: SifipApi = sifipMock

// après (prod)
val sifipApi: SifipApi = RetrofitSifipClient(
    baseUrl = "https://api.34-53-128-84.sslip.io",
    oauthClientId = BuildConfig.SIFIP_CLIENT_ID,
    privateKeyPem = readPemFromAssets("private.pem"),
)
```

Endpoints documentés dans
[`sifip-platform/docs/COLLEAGUE-GUIDE.md` §5](../sifip-platform/docs/COLLEAGUE-GUIDE.md) :

```
POST /number-verification/vwip/verify    scope: sifip:number-verify
POST /sim-swap/vwip/check                scope: sifip:sim-swap
POST /device-swap/vwip/check             scope: sifip:device-swap
POST /fraud-engine/score                 scope: sifip:fraud-engine
```

## 7. Conseils pour la démo en clientèle

1. **Parcours nominal** : login en `ALL_OK`, virement à **250 000 FCFA** → score
   **12 %** ✅, virement autorisé.
2. **Blocage par montant** : refaire un virement à **2 000 000 FCFA** → score
   **76 %** ❌, transaction bloquée (sans changer de scénario, juste le montant).
3. **Blocage explicite** : passer en `FAIL_FRAUD`, n'importe quel montant → score
   **87 %** ❌.
4. **Échecs au login** : démontrer chaque étape SIFIP via `FAIL_NUMBER_VERIFY`,
   `FAIL_SIM_SWAP`, `FAIL_DEVICE_SWAP`.
5. Le délai artificiel (≈ 500 ms par appel) est volontaire pour que les
   animations soient visibles à l'œil nu.

## 8. Licence

Code propriétaire — usage **démo BNI uniquement**. Ne pas distribuer.

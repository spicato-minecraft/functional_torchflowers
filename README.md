# Functional Torchflowers

Fabric mod for Minecraft **26.1**. Mature torchflowers (placed flower and fully grown crop) emit **light level 8**. Bee night work is **not** in this mod — install **Busy Bees** for night torchflower pollination.

## Compatibility

| Component     | Version           |
|---------------|-------------------|
| Minecraft     | 26.1              |
| Fabric Loader | 0.19.3+           |
| Fabric API    | 0.145.1+26.1      |
| Java          | 25+               |

## Build

From this directory:

```bash
./gradlew build
```

The output JAR is at `build/libs/functional_torchflowers-1.0.0.jar` (version from `gradle.properties`).

## Test

```bash
./gradlew test          # unit tests (JUnit + Fabric Loader bootstrap)
./gradlew runGameTest   # server GameTests
./gradlew build         # full CI gate (includes both)
```

Play the indexed local set from [`../dev-pack`](../dev-pack/README.md) (`./gradlew runClient` there).

## Install

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 26.1.
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for 26.1 and place it in your mods folder.
3. Copy the built JAR into your Minecraft `mods` folder.

## Branch Policy

Each supported Minecraft version has its own long-lived branch named after the exact `minecraft_version` in `gradle.properties`. Version branches are the source of truth; `main` is not used.

- **Default branch:** `26.1`
- **Feature/fix work:** branch from the target version as `{version}/feature-name`, merge back into the version branch

```bash
git clone https://github.com/spicato-minecraft/functional_torchflowers.git
cd functional_torchflowers
git checkout 26.1
```

## Releases and Modrinth

Merging code does **not** publish to Modrinth. Publishing happens only when a GitHub Release is created.

Release tags use `{minecraft_version}-{mod_version}` (e.g. `26.1-1.0.0`), matching `gradle.properties`.

1. Merge your changes into the version branch (e.g. `26.1`).
2. Bump `mod_version` in `gradle.properties` if shipping new player-facing changes.
3. Add a changelog at `releases/{tag}.md` — see `releases/TEMPLATE.md`.
4. Create a GitHub Release with tag `{minecraft_version}-{mod_version}` targeting the version branch.

See [minecraft-mods/docs/README.md](../docs/README.md) for org-wide release conventions.

## Test Strategy

| Layer      | Purpose |
|------------|---------|
| Unit tests | Qualifying blocks; light baked on block states (mixins apply in the test environment when the loader is present) |
| GameTests  | Light in-world (8 vs 0) |

Behavior tests are tracked in [Notion Tasks](https://app.notion.com/p/3b716e8d4da380adbd93cf0a73b39d5c). Manual UAT items live in the Mod Catalog.

## Architecture

Vanilla torchflowers emit no light. This mod:

1. Emits light **4–8** (per block, stable) on placed torchflowers; fully grown crop still emits **8**.
2. Leaves bee AI vanilla. Night lantern behavior lives in Busy Bees.

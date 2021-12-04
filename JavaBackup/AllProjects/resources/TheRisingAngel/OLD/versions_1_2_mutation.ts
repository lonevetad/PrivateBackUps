//import fs from "fs";
const fs = require("fs")

type AttributeMod = {
    "attribute": string,
    "value": number
}

type NamedAttributesModifier_V1 = {
    name: string,
    rarity: number,
    price: Array<number>,
    attributeModifiers: Array<AttributeMod>
}

type EquipItem_V1 = NamedAttributesModifier_V1 & {
    type: string,
    dimensInvent: {
        "width": number, "height": number
    }
    , abilities: Array<string>
}

type EquipUpgrade_V1 = NamedAttributesModifier_V1

// .........................

type AttributeModifications = {
    [attribute: string]: number
}

type NamedAttributesModifier_V2 = {
    name: string,
    rarity: number,
    price: Array<number>,
    attributeModifiers: AttributeModifications
}
type EquipItem_V2 = NamedAttributesModifier_V2 & {
    type: string,
    dimensInvent: {
        "width": number, "height": number
    }
    , abilities: Array<string>
}
type EquipUpgrade_V2 = NamedAttributesModifier_V2

// -------------------------------------------------

var equipsUpgrades = JSON.parse(fs.readFileSync("./equipUpgradesTRAr_V1.json", { encoding: "utf-8" })) as Array<EquipUpgrade_V1>;
fs.writeFileSync(
    "./equipUpgradesTRAr.json",
    JSON.stringify(
        equipsUpgrades.map((e, _, __) => {
            var a: AttributeModifications = {}
            for (const m of e.attributeModifiers) {
                a[m.attribute] = m.value
            }
            return {
                name: e.name,
                rarity: e.rarity,
                price: e.price,
                attributeModifiers: a
            } as EquipUpgrade_V2
        })
        , null, 2));

var equips = JSON.parse(fs.readFileSync("./equipItems_V1.json", { encoding: "utf-8" })) as Array<EquipItem_V1>;
fs.writeFileSync(
    "./equipItems.json",
    JSON.stringify(
        equips.map((e, _, __) => {
            var a: AttributeModifications = {}
            for (const m of e.attributeModifiers) {
                a[m.attribute] = m.value
            }
            return {
                name: e.name,
                rarity: e.rarity,
                price: e.price,
                type: e.type,
                dimensInvent: e.dimensInvent,
                abilities: e.abilities,
                attributeModifiers: a
            } as EquipItem_V2
        })
        , null, 2));

#!/usr/bin/env python3
"""Generate the small pixel-art item textures and JSON assets for PVP20.

Run from the repository root with: python3 scripts/generate_assets.py
Only Python's standard library is required.
"""

from __future__ import annotations

import json
import struct
import zlib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "src/main/resources/assets/pvp20"

# id, English name, Russian name, tooltip (English, Russian), icon type, accent color
ITEMS = [
    ("emberfang_blade", "Emberfang Blade", "Клинок Пламенного Клыка", "Hits ignite enemies for 2.5 seconds.", "При ударе поджигает противника на 2,5 секунды.", "sword", (239, 91, 40)),
    ("glacial_rapier", "Glacial Rapier", "Ледяная рапира", "Hits slow enemies for 4 seconds.", "Замедляет противника на 4 секунды.", "sword", (91, 205, 246)),
    ("venom_stiletto", "Venom Stiletto", "Ядовитый стилет", "Hits poison enemies for 5 seconds.", "Отравляет противника на 5 секунд.", "sword", (117, 220, 82)),
    ("siphon_scythe", "Siphon Scythe", "Коса-кровопийца", "Hits restore 1 heart to you.", "Попадание восстанавливает вам 1 сердце.", "scythe", (201, 92, 227)),
    ("stormbreaker_hammer", "Stormbreaker Hammer", "Молот Грозолома", "Hits briefly lift enemies into the air.", "Приподнимает противника в воздух при попадании.", "hammer", (248, 205, 77)),
    ("rift_cleaver", "Rift Cleaver", "Разломный тесак", "Hits weaken enemies for 4 seconds.", "Ослабляет противника на 4 секунды.", "sword", (145, 98, 243)),
    ("bloodrush_katana", "Bloodrush Katana", "Катана Кровавого натиска", "Hits grant you Strength for 3 seconds.", "Даёт вам Силу на 3 секунды при попадании.", "sword", (221, 54, 86)),
    ("anchor_maul", "Anchor Maul", "Якорная кувалда", "Hits slow and weaken enemies.", "Замедляет и ослабляет противника при попадании.", "hammer", (77, 183, 194)),
    ("windstep_charm", "Windstep Charm", "Талисман Ветрогона", "Right-click: dash forward and gain Speed. Cooldown: 10 s.", "ПКМ: рывок вперёд и Скорость. Перезарядка: 10 сек.", "feather", (103, 220, 211)),
    ("guardian_brew", "Guardian Brew", "Эликсир стража", "Right-click: gain Resistance for 6 s. Cooldown: 25 s.", "ПКМ: Сопротивление на 6 сек. Перезарядка: 25 сек.", "potion", (67, 172, 224)),
    ("berserker_serum", "Berserker Serum", "Сыворотка берсерка", "Right-click: gain Strength II for 5 s. Cooldown: 30 s.", "ПКМ: Сила II на 5 сек. Перезарядка: 30 сек.", "potion", (218, 67, 66)),
    ("vitality_elixir", "Vitality Elixir", "Эликсир жизненной силы", "Right-click: heal 3 hearts and regenerate. Cooldown: 20 s.", "ПКМ: лечение на 3 сердца и регенерация. Перезарядка: 20 сек.", "medkit", (230, 81, 111)),
    ("smoke_bomb", "Smoke Bomb", "Дымовая бомба", "Right-click: blind and slow nearby enemies. Cooldown: 15 s.", "ПКМ: ослепляет и замедляет врагов рядом. Перезарядка: 15 сек.", "bomb", (126, 122, 143)),
    ("frost_nova", "Frost Nova", "Ледяная вспышка", "Right-click: slow nearby enemies. Cooldown: 20 s.", "ПКМ: замедляет врагов вокруг. Перезарядка: 20 сек.", "snowflake", (92, 194, 238)),
    ("repulsion_orb", "Repulsion Orb", "Сфера отталкивания", "Right-click: knock back nearby enemies. Cooldown: 14 s.", "ПКМ: отбрасывает врагов рядом. Перезарядка: 14 сек.", "orb", (191, 100, 238)),
    ("hunters_flare", "Hunter's Flare", "Метка охотника", "Right-click: reveal nearby enemies for 8 s. Cooldown: 12 s.", "ПКМ: подсвечивает врагов рядом на 8 сек. Перезарядка: 12 сек.", "flare", (255, 211, 82)),
    ("blink_rune", "Blink Rune", "Руна скачка", "Right-click: evade backward and briefly turn invisible. Cooldown: 13 s.", "ПКМ: уклонение назад и краткая невидимость. Перезарядка: 13 сек.", "rune", (104, 109, 247)),
    ("adrenaline_shot", "Adrenaline Shot", "Укол адреналина", "Right-click: gain Speed II and Strength. Cooldown: 22.5 s.", "ПКМ: Скорость II и Сила. Перезарядка: 22,5 сек.", "syringe", (241, 147, 54)),
    ("binding_sigil", "Binding Sigil", "Сковывающая печать", "Right-click: heavily slow nearby enemies. Cooldown: 16 s.", "ПКМ: сильно замедляет врагов рядом. Перезарядка: 16 сек.", "sigil", (71, 202, 159)),
    ("echo_horn", "Echo Horn", "Рог эха", "Right-click: blast enemies away and gain Resistance. Cooldown: 25 s.", "ПКМ: отбрасывает врагов и даёт Сопротивление. Перезарядка: 25 сек.", "horn", (218, 176, 100)),
]

# Armor reuses vanilla diamond icons and the vanilla diamond equipment asset in code.
ARMOR_ITEMS = [
    ("reinforced_diamond_helmet", "Reinforced Diamond Helmet", "Усиленный алмазный шлем", "4 armor, 3 toughness, 0.1 knockback resistance. 495 durability. Diamond look.", "4 брони, прочность брони 3, сопротивление отбрасыванию 0,1; долговечность 495. Ванильный алмазный вид.", "diamond_helmet"),
    ("reinforced_diamond_chestplate", "Reinforced Diamond Chestplate", "Усиленный алмазный нагрудник", "9 armor, 3 toughness, 0.1 knockback resistance. 720 durability. Diamond look.", "9 брони, прочность брони 3, сопротивление отбрасыванию 0,1; долговечность 720. Ванильный алмазный вид.", "diamond_chestplate"),
    ("reinforced_diamond_leggings", "Reinforced Diamond Leggings", "Усиленные алмазные поножи", "7 armor, 3 toughness, 0.1 knockback resistance. 675 durability. Diamond look.", "7 брони, прочность брони 3, сопротивление отбрасыванию 0,1; долговечность 675. Ванильный алмазный вид.", "diamond_leggings"),
    ("reinforced_diamond_boots", "Reinforced Diamond Boots", "Усиленные алмазные ботинки", "4 armor, 3 toughness, 0.1 knockback resistance. 585 durability. Diamond look.", "4 брони, прочность брони 3, сопротивление отбрасыванию 0,1; долговечность 585. Ванильный алмазный вид.", "diamond_boots"),
]

OUTLINE = (28, 25, 38, 255)
DARK = (71, 66, 83, 255)
STEEL = (171, 190, 204, 255)
LIGHT = (240, 247, 250, 255)
WHITE = (255, 255, 255, 255)


class Canvas:
    def __init__(self, size: int = 16):
        self.size = size
        self.pixels = [[(0, 0, 0, 0) for _ in range(size)] for _ in range(size)]

    def put(self, x: int, y: int, color):
        if 0 <= x < self.size and 0 <= y < self.size:
            self.pixels[y][x] = (*color, 255) if len(color) == 3 else color

    def rect(self, x0: int, y0: int, x1: int, y1: int, color):
        for y in range(y0, y1 + 1):
            for x in range(x0, x1 + 1):
                self.put(x, y, color)

    def line(self, x0: int, y0: int, x1: int, y1: int, color, width: int = 1):
        dx, dy = abs(x1 - x0), abs(y1 - y0)
        sx, sy = (1 if x0 < x1 else -1), (1 if y0 < y1 else -1)
        error = dx - dy
        while True:
            for ox in range(-(width // 2), width - width // 2):
                for oy in range(-(width // 2), width - width // 2):
                    self.put(x0 + ox, y0 + oy, color)
            if x0 == x1 and y0 == y1:
                break
            twice = error * 2
            if twice > -dy:
                error -= dy
                x0 += sx
            if twice < dx:
                error += dx
                y0 += sy

    def circle(self, cx: int, cy: int, radius: int, color, fill: bool = True):
        outer = radius * radius
        inner = (radius - 1) * (radius - 1)
        for y in range(cy - radius, cy + radius + 1):
            for x in range(cx - radius, cx + radius + 1):
                distance = (x - cx) ** 2 + (y - cy) ** 2
                if distance <= outer and (fill or distance >= inner):
                    self.put(x, y, color)

    def diamond(self, cx: int, cy: int, radius: int, color, fill: bool = False):
        for y in range(cy - radius, cy + radius + 1):
            for x in range(cx - radius, cx + radius + 1):
                distance = abs(x - cx) + abs(y - cy)
                if distance <= radius and (fill or distance == radius):
                    self.put(x, y, color)

    def png(self, path: Path):
        raw = b"".join(b"\x00" + bytes(channel for pixel in row for channel in pixel) for row in self.pixels)
        header = struct.pack(">IIBBBBB", self.size, self.size, 8, 6, 0, 0, 0)

        def chunk(kind: bytes, data: bytes) -> bytes:
            payload = kind + data
            return struct.pack(">I", len(data)) + payload + struct.pack(">I", zlib.crc32(payload) & 0xFFFFFFFF)

        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", header) + chunk(b"IDAT", zlib.compress(raw, 9)) + chunk(b"IEND", b""))


def sword_icon(accent):
    c = Canvas()
    accent_dark = tuple(max(0, value - 65) for value in accent)
    accent_light = tuple(min(255, value + 40) for value in accent)
    c.line(4, 12, 13, 3, OUTLINE, 4)
    c.line(5, 11, 12, 4, accent_dark, 2)
    c.line(6, 10, 12, 4, STEEL, 1)
    c.line(7, 9, 12, 4, LIGHT, 1)
    c.line(5, 10, 10, 15, OUTLINE, 2)
    c.line(5, 10, 9, 14, accent, 1)
    c.line(4, 12, 2, 14, DARK, 3)
    c.put(1, 15, accent_light)
    c.put(13, 3, WHITE)
    return c


def hammer_icon(accent):
    c = Canvas()
    shade = tuple(max(0, value - 65) for value in accent)
    light = tuple(min(255, value + 50) for value in accent)
    c.line(5, 7, 11, 14, OUTLINE, 3)
    c.line(5, 7, 10, 13, shade, 1)
    c.line(4, 6, 7, 3, OUTLINE, 4)
    c.rect(6, 2, 13, 7, OUTLINE)
    c.rect(7, 3, 12, 6, shade)
    c.rect(7, 3, 11, 5, accent)
    c.line(7, 3, 11, 3, light, 1)
    c.rect(8, 10, 10, 13, accent)
    c.put(5, 14, DARK)
    return c


def scythe_icon(accent):
    c = Canvas()
    shade = tuple(max(0, value - 65) for value in accent)
    c.line(3, 14, 11, 6, OUTLINE, 3)
    c.line(4, 13, 10, 7, shade, 1)
    c.line(11, 6, 10, 3, OUTLINE, 3)
    c.line(10, 3, 7, 2, OUTLINE, 3)
    c.line(7, 2, 4, 3, OUTLINE, 3)
    c.line(4, 3, 2, 6, OUTLINE, 3)
    c.line(11, 5, 9, 3, LIGHT, 1)
    c.line(9, 3, 6, 3, STEEL, 1)
    c.line(6, 3, 4, 4, accent, 1)
    c.put(2, 15, accent)
    return c


def potion_icon(accent):
    c = Canvas()
    shade = tuple(max(0, value - 55) for value in accent)
    light = tuple(min(255, value + 50) for value in accent)
    c.rect(6, 1, 9, 4, OUTLINE)
    c.rect(6, 2, 9, 3, shade)
    c.rect(5, 4, 10, 5, OUTLINE)
    c.rect(4, 5, 11, 12, OUTLINE)
    c.rect(5, 6, 10, 11, shade)
    c.rect(5, 8, 10, 11, accent)
    c.rect(6, 6, 7, 7, light)
    c.rect(4, 12, 11, 13, DARK)
    c.put(7, 9, light)
    c.put(8, 9, light)
    return c


def draw_icon(kind, accent):
    if kind == "sword":
        return sword_icon(accent)
    if kind == "hammer":
        return hammer_icon(accent)
    if kind == "scythe":
        return scythe_icon(accent)
    if kind in {"potion", "syringe"}:
        if kind == "potion":
            return potion_icon(accent)
        c = Canvas()
        shade = tuple(max(0, value - 55) for value in accent)
        c.line(3, 13, 11, 5, OUTLINE, 4)
        c.line(4, 12, 10, 6, STEEL, 2)
        c.line(5, 11, 9, 7, accent, 1)
        c.rect(1, 12, 4, 14, OUTLINE)
        c.rect(2, 12, 3, 13, accent)
        c.rect(10, 3, 13, 6, OUTLINE)
        c.rect(11, 3, 12, 5, shade)
        c.put(13, 2, WHITE)
        return c
    c = Canvas()
    shade = tuple(max(0, value - 60) for value in accent)
    light = tuple(min(255, value + 48) for value in accent)

    if kind == "feather":
        c.line(3, 13, 12, 3, OUTLINE, 4)
        c.line(4, 12, 11, 4, accent, 2)
        c.line(5, 11, 10, 5, light, 1)
        c.line(4, 9, 8, 5, shade, 1)
        c.line(7, 12, 11, 8, shade, 1)
        c.line(2, 14, 6, 10, DARK, 1)
    elif kind == "medkit":
        c.rect(3, 4, 12, 13, OUTLINE)
        c.rect(4, 5, 11, 12, (225, 225, 232))
        c.rect(6, 1, 9, 4, OUTLINE)
        c.rect(7, 2, 8, 3, shade)
        c.rect(7, 6, 8, 11, accent)
        c.rect(5, 8, 10, 9, accent)
        c.put(5, 6, WHITE)
    elif kind == "bomb":
        c.line(8, 3, 11, 1, OUTLINE, 2)
        c.put(12, 1, light)
        c.circle(7, 9, 5, OUTLINE)
        c.circle(7, 9, 4, shade)
        c.circle(7, 9, 3, accent)
        c.rect(5, 5, 6, 6, light)
        c.put(3, 11, DARK)
    elif kind == "snowflake":
        c.circle(8, 8, 6, OUTLINE, fill=False)
        for x0, y0, x1, y1 in ((8, 2, 8, 14), (2, 8, 14, 8), (4, 4, 12, 12), (4, 12, 12, 4)):
            c.line(x0, y0, x1, y1, accent, 1)
        for x, y in ((5, 4), (11, 4), (5, 12), (11, 12), (6, 6), (10, 6), (6, 10), (10, 10)):
            c.put(x, y, light)
        c.circle(8, 8, 1, WHITE)
    elif kind == "orb":
        c.circle(8, 8, 6, OUTLINE)
        c.circle(8, 8, 4, shade)
        c.circle(8, 8, 3, accent)
        c.circle(6, 6, 1, light)
        c.line(1, 8, 3, 8, WHITE, 1)
        c.line(13, 8, 15, 8, WHITE, 1)
        c.line(8, 1, 8, 3, WHITE, 1)
        c.line(8, 13, 8, 15, WHITE, 1)
    elif kind == "flare":
        c.line(8, 1, 8, 14, OUTLINE, 3)
        c.line(2, 8, 14, 8, OUTLINE, 3)
        c.line(4, 4, 12, 12, accent, 2)
        c.line(4, 12, 12, 4, accent, 2)
        c.line(8, 2, 8, 13, light, 1)
        c.line(3, 8, 13, 8, light, 1)
        c.circle(8, 8, 2, WHITE)
    elif kind == "rune":
        c.diamond(8, 8, 7, OUTLINE)
        c.diamond(8, 8, 5, shade)
        c.diamond(8, 8, 3, accent)
        c.line(8, 3, 8, 13, light, 1)
        c.line(4, 8, 12, 8, light, 1)
        c.put(8, 8, WHITE)
    elif kind == "sigil":
        c.circle(8, 8, 6, OUTLINE, fill=False)
        c.circle(8, 8, 4, accent, fill=False)
        c.diamond(8, 8, 3, OUTLINE)
        c.diamond(8, 8, 2, light, fill=True)
        c.line(2, 8, 5, 8, light, 1)
        c.line(11, 8, 14, 8, light, 1)
        c.put(8, 8, WHITE)
    elif kind == "horn":
        c.line(3, 12, 11, 4, OUTLINE, 4)
        c.line(4, 12, 10, 5, shade, 2)
        c.line(5, 11, 9, 6, accent, 1)
        c.line(10, 4, 13, 2, OUTLINE, 4)
        c.line(10, 4, 13, 2, light, 1)
        c.rect(1, 12, 4, 14, OUTLINE)
        c.rect(2, 12, 3, 13, accent)
        c.put(14, 1, WHITE)
        c.put(14, 3, light)
    else:
        raise ValueError(f"Unknown icon kind: {kind}")
    return c


def write_json(path: Path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


def main():
    en_us = {"creativeTab.pvp20.arsenal": "Duelist's Arsenal"}
    ru_ru = {"creativeTab.pvp20.arsenal": "Арсенал дуэлянта"}

    for item_id, en_name, ru_name, en_lore, ru_lore, icon_kind, accent in ITEMS:
        en_us[f"item.pvp20.{item_id}"] = en_name
        ru_ru[f"item.pvp20.{item_id}"] = ru_name
        en_us[f"tooltip.pvp20.{item_id}"] = en_lore
        ru_ru[f"tooltip.pvp20.{item_id}"] = ru_lore

        model_parent = "minecraft:item/handheld" if icon_kind in {"sword", "hammer", "scythe"} else "minecraft:item/generated"
        write_json(ASSETS / "models/item" / f"{item_id}.json", {
            "parent": model_parent,
            "textures": {"layer0": f"pvp20:item/{item_id}"},
        })
        write_json(ASSETS / "items" / f"{item_id}.json", {
            "model": {"type": "minecraft:model", "model": f"pvp20:item/{item_id}"},
        })
        draw_icon(icon_kind, accent).png(ASSETS / "textures/item" / f"{item_id}.png")

    for item_id, en_name, ru_name, en_lore, ru_lore, vanilla_icon in ARMOR_ITEMS:
        en_us[f"item.pvp20.{item_id}"] = en_name
        ru_ru[f"item.pvp20.{item_id}"] = ru_name
        en_us[f"tooltip.pvp20.{item_id}"] = en_lore
        ru_ru[f"tooltip.pvp20.{item_id}"] = ru_lore

        write_json(ASSETS / "models/item" / f"{item_id}.json", {
            "parent": "minecraft:item/generated",
            "textures": {"layer0": f"minecraft:item/{vanilla_icon}"},
        })
        write_json(ASSETS / "items" / f"{item_id}.json", {
            "model": {"type": "minecraft:model", "model": f"pvp20:item/{item_id}"},
        })

    write_json(ASSETS / "lang/en_us.json", en_us)
    write_json(ASSETS / "lang/ru_ru.json", ru_ru)
    print(f"Generated {len(ITEMS) + len(ARMOR_ITEMS)} items: {len(ITEMS)} custom icons and {len(ARMOR_ITEMS)} vanilla-textured armor pieces.")


if __name__ == "__main__":
    main()

package com.github.antilaby.antilaby.lang;

import com.github.antilaby.antilaby.main.AntiLaby;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;

public enum Locale {
  VI_VN, EN_UD, LV_LV, LA_LA, NDS_DE, HR_HR, LT_LT, GD_GB, GYA_AA, FIL_PH, NL_NL, HE_IL, JBO_EN,
  MN_MN, SWG_DE, FR_FR, EN_US, EN_GB, KSH_DE, FR_CA, HAW_US, GV_IM, ES_ES, GA_IE, EN_PT, IO_IDO,
  BE_BY, EN_AU, IT_IT, AR_SA, DE_AT, GL_ES, EL_GR, CS_CZ, BG_BG, TLH_AA, HI_IN, LI_LI, ES_MX, EU_ES,
  TZL_TZL, PL_PL, KA_GE, ES_AR, KW_GB, SE_NO, HY_AM, EN_CA, TH_TH, TR_TR, FI_FI, SQ_AL, DA_DK,
  EO_UY, IS_IS, SV_SE, FO_FO, MI_NZ, ID_ID, PT_PT, VAL_ES, NN_NO, LB_LU, DE_DE, PT_BR, RU_RU, SO_SO,
  HU_HU, SR_SP, OC_FR, MK_MK, SL_SI, NO_NO, ZH_TW, BR_FR, AF_ZA, FY_NL, ET_EE, MS_MY, AZ_AZ, ES_UY,
  AST_ES, FA_IR, SK_SK, KO_KR, MT_MT, CA_ES, ES_VE, CY_GB, EN_NZ, JA_JP, RO_RO, LOL_US, ZH_CN,
  UK_UA, UNDEFINED;

  private static FileSystem fs;
  private final Map<String, String> translation = new HashMap<>();

  static void closeInput() throws IOException {
    if (fs != null) {
      fs.close();
    }
  }

  private static FileSystem getFs() throws IOException {
    if (fs == null) {
      HashMap<String, Object> hm = new HashMap<>();
      hm.put("create", true);
      fs = FileSystems.newFileSystem(URI.create("jar:file:/"
          + AntiLaby.getInstance().getPath().toAbsolutePath().toString().replace('\\', '/')), hm);
    }
    return fs;
  }

  public static Locale byName(String name, Locale fallback) {
    try {
      return valueOf(name.toUpperCase(java.util.Locale.ROOT));
    } catch (Exception e) {
      return fallback;
    }
  }

  private static Map<String, String> parse(Path path) throws IOException {
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    String ln;
    final Map<String, String> result = new HashMap<>(10);
    for (int line = 0; line < lines.size(); line++) {
      ln = lines.get(line).trim();
      if (!ln.startsWith("#")) {
        if (!ln.contains("=")) {
          LanguageManager.LOG.warn("Could not parse line " + (1 + line) + " in resource " + path.getFileName() + ": " + ln);
        } else {
          final String[] s = ln.split("=", 2);
          String value = s[1].replaceFirst("#.*", "").trim();
          result.put(s[0].trim().replace(" ", ""), value);
        }
      }
    }
    return result;
  }

  private boolean isNoOp() {
    if (this == UNDEFINED) {
      return true;
    }
    AntiLaby pl = AntiLaby.getInstance();
    try {
      return !(Files.isRegularFile(pl.getDataPath().resolve("lang/" + this + ".lang"))
          || Files.isRegularFile(getFs().getPath("assets/antilaby/lang/" + this + ".lang")));
    } catch (IOException e) {
      return true;
    }
  }

  @Override
  public String toString() {
    return name().toLowerCase(java.util.Locale.ROOT);
  }

  private String format(String toFormat, Object... args) {
    for (int i = 0; i < args.length; i++) {
      toFormat = toFormat.replace("%" + (i + 1), args[i].toString());
    }
    return toFormat;
  }

  public String translate(String toTranslate, Object... args) {
    if (isNoOp()) {
      return EN_US.translate(toTranslate, args);
    }
    if (translation.containsKey(toTranslate)) {
      return ChatColor.translateAlternateColorCodes('&',
          format(translation.get(toTranslate), args));
    }
    if (EN_US.translation.containsKey(toTranslate)) {
      return EN_US.translate(toTranslate, args);
    }
    return translation.getOrDefault("translation.error",
        EN_US.translation.getOrDefault("translation.error", toTranslate));
  }

  void init() throws IOException {
    AntiLaby plugin = AntiLaby.getInstance();
    if (isNoOp()) {
      return;
    }
    final Path path = plugin.getDataPath().resolve("lang/" + this + ".lang");
    if (!Files.isRegularFile(path)) {
      if (Files.exists(path)) {
        Files.delete(path);
      }
      Files.copy(getFs().getPath("assets/antilaby/lang/" + this + ".lang"), path);
    }
    translation.putAll(parse(path));
  }
}
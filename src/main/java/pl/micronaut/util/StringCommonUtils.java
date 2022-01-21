package pl.micronaut.util;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;

public class StringCommonUtils {

  public static String valueOf(String value) {
    return StringUtils.isNotBlank(value) ? value : "";
  }

  public static String objectToString(@NotBlank String lombokToString, boolean omitClassName) {
    lombokToString =
        lombokToString
            .replaceAll("(?<=(, |\\())[^\\s(]+?=null(?:, )?", "")
            .replaceFirst(", \\)$", ")");
    lombokToString =
        omitClassName
            ? lombokToString.replaceFirst("^\\w+\\(?", "").replaceFirst("\\)?$", "")
            : lombokToString;
    return lombokToString;
  }
}

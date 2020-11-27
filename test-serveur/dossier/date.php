<?php
    setlocale(LC_ALL, "fr_FR");
    $str = strftime("%A %e %B %Y à %H:%M");
    $encoding = mb_detect_encoding($str, "auto", true);
    $converted = iconv($encoding, "UTF-8", $str);
    $converted = str_replace("  ", " ", $converted);
    echo $converted . PHP_EOL;
?>
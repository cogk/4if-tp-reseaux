<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link href="racines.css" rel="stylesheet" />
  <style>
    html, algo u, algo em, input {
      animation-delay: -<?php echo rand(0, 30); ?>s;
      /* animation-duration: 60s; */
    }
  </style>
</head>

<body>
<h1>Racines.php</h1>
<h2>Calculateur de racines réelles d’un polynôme du second degré à coefficients réels</h2>

<form>
  <input name="a" type="number"
    value="<?php echo isset($_GET['a']) ? $_GET['a'] : 0; ?>"
    min="-10000" max="+10000" />
    .x² +
  <input name="b" type="number"
    value="<?php echo isset($_GET['b']) ? $_GET['b'] : 0; ?>"
    min="-10000" max="+10000" />
    .x +
  <input name="c" type="number"
    value="<?php echo isset($_GET['c']) ? $_GET['c'] : 0; ?>"
    min="-10000" max="+10000" />

  <input type="submit" value="Go !" />
</form>

<?php
    function afficher($text)
    {
        echo "<div>{$text}</div>";
    }

    if (isset($_GET["a"])
        and isset($_GET["b"])
        and isset($_GET["c"])
      ) {
        $a = $_GET["a"];
        $b = $_GET["b"];
        $c = $_GET["c"];

        afficher("Polynôme : {$a}x² + {$b}x + {$c}");

        $delta = $b*$b - 4*$a*$c;
        if ($delta > 0) {
            $x1 = (-$b - sqrt($delta)) / (2 * $a);
            $x2 = (-$b + sqrt($delta)) / (2 * $a);
            afficher("Deux racines : <strong>{$x1}</strong> et <strong>${x2}</strong>");
        } elseif ($delta == 0) {
            $x12 = -$b / (2 * $a);
            afficher("Une seule racine : <strong>{$x12}</strong>");
        } else /*if ($delta < 0)*/ {
        afficher("Deux racines complexes conjuguées");
      }
    }
?>

<h3>Algorithme</h3>

<algo>
<h4>Variables</h4>
  <em>a</em>, <em>b</em> et <em>c</em> réels donnés en entrée
  <em>Δ</em> réel

<h4>Entrée</h4>
  Entrer <em>a</em>
  Entrer <em>b</em>
  Entrer <em>c</em>

<h4>Traitement et résultats</h4>
  Afficher <u>"Polynôme : <em>a</em>x² + <em>b</em>x + <em>c</em>"</u>
  Affecter la valeur (<em>b</em>² - 4<em>a</em><em>c</em>) à la variable <em>Δ</em>

  Si <strong><em>Δ</em> > 0</strong>, alors
    Afficher <u>"x1 = "</u>, (-<em>b</em> - √<em>Δ</em>) / 2<em>a</em>
    Afficher <u>"x2 = "</u>, (-<em>b</em> + √<em>Δ</em>) / 2<em>a</em>
  Sinon si <strong><em>Δ</em> = 0</strong>, alors
    Afficher <u></u>"x1,2 = "</u>, -<em>b</em> / 2<em>a</em>
  Sinon // <strong><em>Δ</em> négatif</strong>
    Afficher <u>"Deux racines complexes"</u>

<h4>Fin</h4>
</algo>

</body>
</html>

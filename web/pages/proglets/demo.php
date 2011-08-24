<?php
if (!isset($_GET['id']))
    die('Error');
$id = $_GET['id'];
Sal::validateProgletId($id);
if (!is_file("proglets/" . $id . "/proglet.php"))
    die("La proglet " . $id . " n'a pas de fichier proglet.php");
$proglet = null;
include("proglets/" . $id . "/proglet.php");  //TODO testme
if (isset($proglet['name']))
    $name = $proglet['name']; else
    $name="";
if (isset($proglet['description']))
    $desc = $proglet['description']; else
    $desc="";
if (isset($proglet['icon']))
    $icon = 'proglets/' . $id . '/' . $proglet['icon']; else
    $icon="";
if ($name == "")
    $name = $id;

$defaulticon = "images/defaultProglet.png";

if ($icon == "")
    $icon = $defaulticon;
if (!is_file($icon))
    $icon = $defaulticon;
?>

<?php showBrowser(array(array("Java's Cool", "index.php"), array("Proglets", "index.php?page=proglets"), array($name, ""))); ?>

<table>
    <tr>
        <td class="proglet">
            <span><?php echo $name; ?></span>
            <span class="proglet-image"><img src="<?php echo($icon); ?>" alt=""/></span>
        </td>
    </tr>
</table>
<br />

<?php showButton(array('Voir la documentation', '?page=proglets&action=show&id=' . $id)); ?>
<div class="news">
    <table class="news">
        <tr class="news-top">
            <td colspan="5" class="news-top"></td>
        </tr>
        <tr class="news-center">
            <td class="news-left"></td>
            <td class="news-leftborder"></td>
            <td class="news-center">
		<?php include('proglets/'.$id.'/applet-tag.htm'); ?>
	    </td>
            <td class="news-rightborder"></td>
            <td class="news-right"></td>
        </tr>
        <tr class="news-bottom">
            <td colspan="5" class="news-bottom"></td>
        </tr>
    </table>
</div>

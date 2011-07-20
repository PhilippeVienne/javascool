<?php

/*
 * Security abstraction layer
 */
class Sal {
    public static function validatePage($page) {
        if (!preg_match('#^[a-z][a-z0-9]+$#', $page))
            die("Error : page name not valid");
    }

    public static function validateAction($action) {
        if (!preg_match('#^[a-z][a-z0-9]+$#', $action))
            die("Error : action name not valid");
    }

    public static function validateProgletId($progletId) {
        if (!preg_match('#^[a-z][a-z0-9]+$#', $progletId))
            die("Error : proglet id not valid");
    }

    public static function progletIdToName($progletId) {
        if ($progletId == 'game')
            return 'Jeux';
        else if ($progletId == 'ingredients')
            return 'Ingr&eacute;dients';
    }
}
?>

<!DOCTYPE html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Hangman</title>
    <link rel="stylesheet" href="resources/css/normalize.css">
    <link rel="stylesheet" href="resources/css/main.css">
    <link rel="stylesheet" href="resources/css/hangman.css">
    <script src="resources/js/vendor/jquery-1.10.2.min.js"></script>
    <script src="resources/js/hangman.js"></script>
    <script type="text/javascript">
        const config = {
            path: "${pageContext.request.contextPath}/"
        };

        $(function() {
            Hangman.loadState();
            Hangman.startGame();
            Hangman.handleManagementPage();
        })
    </script>
</head>
<body>

<div id="container">
    <div id="buttons">
        <div><a href="#" id="start-game">New game</a></div>
        <div><a href="#" id="open-management">Management</a></div>
    </div>

    <div id="box">
        <div id="gallows-bot" class="absolute black-bg hidden"></div>
        <div id="gallows-top" class="absolute black-bg hidden"></div>
        <div id="hangman">
            <div id="rope" class="absolute black-bg hidden"></div>
            <div id="hangman-head" class="absolute black-bg hidden"></div>
            <div id="hangman-left-hand" class="absolute black-bg hidden"></div>
            <div id="hangman-right-hand" class="absolute black-bg hidden"></div>
            <div id="hangman-body" class="absolute black-bg hidden"></div>
            <div id="hangman-left-leg" class="absolute black-bg hidden"></div>
            <div id="hangman-right-leg" class="absolute black-bg hidden"></div>
        </div>
    </div>

    <div id="word"></div>

    <div id="alphabet" class="alphabet hidden"></div>

    <div id="yupi">Yupi!</div>

    <div id="management">

        <div id="management-table"></div>
        <div class="close">X</div>

    </div>
</div>

</body>
</html>

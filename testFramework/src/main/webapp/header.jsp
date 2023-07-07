<%--
  Created by IntelliJ IDEA.
  User: Sanda
  Date: 06/07/2023
  Time: 18:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="contains">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid contains2">
            <br>
            <button class="navbar-toggler" style="margin-top: 3%; margin-bottom: 3%; padding: 1% 1%;" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <div class="Logo">
                    <div><h2 id="bt_title">Test Framework</h2></div>
                </div>

                <div class="navigs">
                    <div class="head_cont">
                        <a class="flex-sm-fill text-sm-center nav-link" class="nav_text">Accueil</a>
                    </div>
                    <span></span>
                </div>


                <div class="navigs">
                    <div class="head_cont">
                        <div class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" class="nav_text" data-bs-toggle="dropdown" role="button" aria-expanded="false" href="?page=Pages">Pages</a>
                            <ul class="dropdown-menu" >
                                <li><a class="dropdown-item" class="nav_page_text" href="?page=Film">Film</a></li>
                                <li><a class="dropdown-item" class="nav_page_text" href="?page=Food">Food</a></li>
                                <li><a class="dropdown-item" class="nav_page_text" href="?page=Musiques">Musiques</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" class="nav_page_text" href="?page=Autres">Autres ...</a></li>
                            </ul>
                        </div>
                    </div>
                    <span></span>
                </div>

                <div class="navigs">
                    <div class="head_cont">
                        <a class="flex-sm-fill text-sm-center nav-link" class="nav_text" href="?page=Contact">Contact</a>
                    </div>
                    <span></span>
                </div>

                <div class="navigs">
                    <div class="head_cont">
                        <a class="flex-sm-fill text-sm-center nav-link" class="nav_text" href="?page=Social media">Social media</a>
                    </div>
                    <span></span>
                </div>
            </div>
        </div>
    </nav>
</div>

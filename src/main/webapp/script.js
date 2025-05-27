window.onload = function () {
    //burger
    $('.burger_menu').on('click', function(){
        $('body').toggleClass('menu_active');
    });

    // for partners
    // сликер для двух полосок картинок, причем они не должны листаться синхронно + они листаются автоматически
    let setTimer;
    const partners = document.querySelector('.autoplay').innerHTML;
    let start = false;
    function slicker() {
        let sl_w = $('.partner:eq(0)').width(),
            cols = Math.round(window.innerWidth/sl_w) + 2;
        for(let i = 0; i < Math.round(cols / 3) + 1; i++)
            $('.autoplay, .autoplay2').append(partners);
  
        console.log(cols)
        if (start) {
            $('.autoplay').slick('unslick');
            $('.autoplay2').slick('unslick');
        }
        
        $('.autoplay').slick({
            infinite: true,
            slidesToShow: cols,
            slidesToScroll: 1,
            autoplay: true,
            autoplaySpeed: 2000,
            variableWidth: true
        });
        setTimeout(function(){
          $('.autoplay2').slick({
            infinite: true,
            slidesToShow: cols,
            slidesToScroll: 1,
            autoplay: true,
            autoplaySpeed: 2000,
            variableWidth: true
          });
        },800);
  
        sl_w = $('.partner:eq(0)').width();
        $('#companies .slick:eq(0)').css('margin-left', -sl_w + "px");
        $('#companies .slick:eq(1)').css('margin-left', -(sl_w / 2) + "px");
    }
    slicker();
    start = true;
    window.addEventListener("resize", function () {
        clearTimeout(setTimer);
        setTimer = setTimeout(() => { slicker(); }, 500);
    });
  //

    // for tarifs
    // при наведении блок увеличивается
    $('.tarif_category:not(.active)').hover(function () {
      $('.tarif_category.active').removeClass('active');
    });
    $( ".tarif_category:not(.active)").on( "mouseleave", function() {
      $('.tarif_category:eq(1)').addClass('active');
    } );
  //

    // for reviews
    // для того чтобы какие-то отзывы показывались а какие-то нет + для подсчета там при пролистывании
    $(".a").css('height', $('.aa > div:eq(0)').height());
    function aa(p){
        console.log(p)
        $('.aa > div').css('opacity', '0');
        setTimeout(function(){ $('.aa > div').css('display', 'block'); }, 0);
        $('.aa > div:eq(' + p + ')').css('display', 'block');
        setTimeout(function(){ $('.aa > div:eq(' + p + ')').css('opacity', '1'); }, 0);
        
        setTimeout(function(){
            $(".a").animate({
                'height': $('.aa > div:eq(' + p + ')').height()
            }, 300, "linear");
        }, 100);
  
        $('.ednum').html((p+1).toString().padStart(2, '0'))
    }
  
    // для листалки
    p = 0, pl = $('.aa > div').length - 1;
    $('.b1').on('click', function(){
        if(p == 0) p = pl;
        else p--;
        aa(p);
    });
    $('.b2').on('click', function(){
  
        if(p == pl) p = 0;
        else p++;
        aa(p);
    });
  //

    // for FAQ
    $('#AskList > div').on('click', function(){
        $('#AskList > div').removeClass('active');
        $(this).addClass('active');
    });
    
    //fetch для формы
    /*
    const form = document.getElementById("form_send");
    const status = document.getElementById("status");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();
        
       // console.log(authorized);
        if (!document.getElementById("oznakomlen").checked) {
            status.textContent = "Вы должны согласиться на обработку данных.";
            status.style.color = "red";
            return;
        }

        const formData = {
            name: document.getElementById("name").value.trim(),
            phone: document.getElementById("phone").value.trim(),
            email: document.getElementById("email").value.trim(),
            message: document.getElementById("message").value.trim()
        };
        try {
            const response = await fetch("api/contact", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formData)
            });

            const result = await response.json();

            if (response.ok) {
                const button = document.getElementById("send");
                button.textContent = "Изменить данные";
                const authorized = localStorage.getItem("authorized") === "true";
                if(authorized)
                    status.textContent = "Сообщение успешно изменено!";
                else
                    status.textContent = "Сообщение успешно отправлено!";
                localStorage.setItem("authorized", "true");
                status.style.color = "green";
            } else {
                status.textContent = result.message || "Ошибка при отправке формы.";
                status.style.color = "red";
            }

        } catch (error) {
            status.textContent = "Ошибка сети. Попробуйте позже.";
            status.style.color = "red";
            console.error("Ошибка:", error);
        }
        
    });
     */
    const form = document.getElementById("form_send");
    const status = document.getElementById("status");

form.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Очистка прошлых сообщений
    clearFieldErrors();

    if (!document.getElementById("oznakomlen").checked) {
        status.textContent = "Вы должны согласиться на обработку данных.";
        status.style.color = "red";
        return;
    }

    const formData = {
        name: document.getElementById("name").value.trim(),
        phone: document.getElementById("phone").value.trim(),
        email: document.getElementById("email").value.trim(),
        message: document.getElementById("message").value.trim()
    };

    try {
        const response = await fetch("api/contact", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(formData)
        });

        const result = await response.json();

        if (response.ok) {
                    /*
            const button = document.getElementById("send");
            button.textContent = "Изменить данные";
            const authorized = localStorage.getItem("authorized") === "true";

            status.textContent = authorized
                ? "Сообщение успешно изменено!"
                : "Сообщение успешно отправлено!";
            status.style.color = "green";
            localStorage.setItem("authorized", "true");
            
            alert(`Логин: ${result.login}\nПароль: ${result.password}`);
             */
            const button = document.getElementById("send");
            button.textContent = "Изменить данные";
            const authorized = sessionStorage.getItem("authorized") === "true";

            status.style.color = "green";

            status.textContent = authorized
                ? "Сообщение успешно изменено!"
                : `Сообщение успешно отправлено! Логин: ${result.login}, Пароль: ${result.password}`;
                
            sessionStorage.setItem("authorized", "true");
        } else {
            // Вывод ошибок по полям
            for (const field in result) {
                const input = document.getElementById(field);
                if (input) {
                    const errorElem = document.createElement("div");
                    errorElem.className = "field-error";
                    errorElem.style.color = "red";
                    errorElem.textContent = result[field];
                    input.parentElement.appendChild(errorElem);
                }
            }
            status.textContent = "Проверьте корректность введённых данных.";
            status.style.color = "red";
        }

    } catch (error) {
        status.textContent = "Ошибка сети. Попробуйте позже.";
        status.style.color = "red";
        console.error("Ошибка:", error);
    }
});

function clearFieldErrors() {
    document.querySelectorAll(".field-error").forEach(el => el.remove());
}

const loginForm = document.getElementById("loginForm");

loginForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const data = {
    username: loginForm.username.value.trim(),
    password: loginForm.password.value.trim()
  };

  try {
    const response = await fetch("api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    const result = await response.json();

    if (response.ok) {
      // Успешная авторизация
      console.log("Успешный вход:", result);
      alert("Вы вошли!");
      sessionStorage.setItem("authorized", "true");
    } else {
      // Ошибка авторизации
      console.error("Ошибка входа:", result.message);
      alert(result.message || "Ошибка при входе");
    }
  } catch (error) {
    console.error("Ошибка сети:", error);
    alert("Ошибка сети. Попробуйте позже.");
  }
});


};

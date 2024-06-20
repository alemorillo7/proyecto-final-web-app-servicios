// FUNCION PARA LA CALIFICACION DE ESTRELLAS
document.addEventListener('DOMContentLoaded', (event) => {
    const stars = document.querySelectorAll('.star-rating label');
    
    stars.forEach(star => {
      star.addEventListener('mouseover', (e) => {
        const onStar = parseInt(e.target.getAttribute('for').substring(4));
        highlightStars(onStar);
      });
  
      star.addEventListener('mouseout', (e) => {
        resetStars();
      });
  
      star.addEventListener('click', (e) => {
        const clickedStar = parseInt(e.target.getAttribute('for').substring(4));
        setStars(clickedStar);
      });
    });
  
    function highlightStars(count) {
      stars.forEach((star, index) => {
        if (index < count) {
          star.style.color = '#FFD700';
        } else {
          star.style.color = '#ddd';
        }
      });
    }
  
    function resetStars() {
      const checkedStar = document.querySelector('.star-rating input[type="radio"]:checked');
      if (checkedStar) {
        const checkedValue = parseInt(checkedStar.value);
        setStars(checkedValue);
      } else {
        stars.forEach(star => star.style.color = '#ddd');
      }
    }
  
    function setStars(count) {
      stars.forEach((star, index) => {
        if (index < count) {
          star.style.color = '#FFD700';
        } else {
          star.style.color = '#ddd';
        }
      });
    }
  
    // Set initial stars based on checked value
    resetStars();
  });
  
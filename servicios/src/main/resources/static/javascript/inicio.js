tippy('#agua', {
    content: 'Agua',
  });

  tippy('#carpinteria', {
    content: 'Carpinteria',
  });

  tippy('#limpieza', {
    content: 'Limpieza',
  });

  tippy('#pintura', {
    content: 'Pintura',
  });

  tippy('#albanileria', {
    content: 'Albañileria',
  });

  tippy('#jardineria', {
    content: 'Jardineria',
  });

  tippy('#reparaciones', {
    content: 'Pequeñas reparaciones',
  });

  tippy('#gas', {
    content: 'Gas',
  });

  tippy('#electricidad', {
    content: 'Electricidad',
  });

  tippy('#electrodomesticos', {
    content: 'Electrodomesticos',
  });

  tippy('#informatica', {
    content: 'Informatica',
  });

  tippy('#aire-acondicionado', {
    content: 'Aire acondicionado',
  });

  tippy('#techo', {
    content: 'Techo',
  });

  tippy('#decoracion', {
    content: 'Decoración',
  });

  tippy('#herreria-soldadura', {
    content: 'Herrería y Soldadura',
  });
  
  tippy('#plomeria', {
    content: 'Plomeria',
  });

  tippy('#arquitectura-construccion', {
    content: 'Arquitectura y Construcción',
  });

  tippy('#cerrajeria', {
    content: 'Cerrajería',
  });

  tippy('#otros', {
    content: 'Otros',
  });

  document.querySelectorAll('.gif-hover').forEach(img => {
    const staticSrc = img.src;
    const gifSrc = img.dataset.gif;

    img.addEventListener('mouseenter', () => {
        img.src = gifSrc;
    });

    img.addEventListener('mouseleave', () => {
        img.src = staticSrc;
    });
});
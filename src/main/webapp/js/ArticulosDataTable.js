$(document).ready(function() {
    // Inicializar DataTable con todas las configuraciones combinadas
    var table = $('#articlesTable').DataTable({
        "paging": false,        // Desactiva la paginación
        "searching": true,      // Mantiene la barra de búsqueda si la estás usando
        "info": false,          // Oculta la información de registros
        "ordering": true,       // Mantiene la opción de ordenar columnas si la estás usando
        "scrollY": "400px",     // Altura máxima de la tabla, con scroll vertical
        "scrollX": true,        // Habilita el scroll horizontal
        "scrollCollapse": true, // Permite que el scroll se colapse si los datos son menores que la altura definida
        "search": {
            "regex": true,      // Habilita la búsqueda con expresiones regulares
            "smart": false      // Desactiva la búsqueda inteligente
        }
    });

    // Modificar el comportamiento de la barra de búsqueda
    $('#articlesTable_filter input').on('input', function() {
        var searchTerm = $(this).val();
        if (searchTerm) {
            searchTerm = '^' + searchTerm + '$'; // Modifica el término de búsqueda para que coincida exactamente
        }
        table.search(searchTerm).draw();
    });
});

document.getElementById('exportExcel').addEventListener('click', function() {
    var selectedRows = [];
    document.querySelectorAll('input[name="seleccionar"]:checked').forEach(function(checkbox) {
        var row = checkbox.closest('tr');
        var cells = row.querySelectorAll('td');
        var rowData = [];
        cells.forEach(function(cell, index) {
            if (index > 0) {
                rowData.push(cell.textContent.trim());
            }
        });
        selectedRows.push(rowData);
    });
    if (selectedRows.length === 0) {
        alert("No hay filas seleccionadas para exportar.");
        return;
    }

    var wb = XLSX.utils.book_new();
    var ws = XLSX.utils.aoa_to_sheet([
        ["No. Inventario", "Nombre", "Marca y modelo", "Serie", "Especificaciones", "Edificio", "Instalación", "Asignado"]
    ].concat(selectedRows));
    XLSX.utils.book_append_sheet(wb, ws, "Sheet1");
    XLSX.writeFile(wb, 'ListaDeArticulos.xlsx');
});

document.getElementById('exportPDF').addEventListener('click', function() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    var selectedRows = [];
    document.querySelectorAll('input[name="seleccionar"]:checked').forEach(function(checkbox) {
        var row = checkbox.closest('tr');
        var cells = row.querySelectorAll('td');
        var rowData = [];
        cells.forEach(function(cell, index) {
            if (index > 0) {
                rowData.push(cell.textContent.trim());
            }
        });
        selectedRows.push(rowData);
    });
    if (selectedRows.length === 0) {
        alert("No hay filas seleccionadas para exportar.");
        return;
    }

    doc.autoTable({
        head: [['No. Inventario', 'Nombre', 'Marca y modelo', 'Serie', 'Especificaciones', 'Edificio', 'Instalación', 'Asignado']],
        body: selectedRows,
        theme: 'grid',
        styles: { fontSize: 8 }
    });

    doc.save('ListaDeArticulos.pdf');
});

document.getElementById('select-all').addEventListener('change', function() {
    const checkboxes = document.querySelectorAll('input[name="seleccionar"]');
    checkboxes.forEach(checkbox => {
        checkbox.checked = this.checked;
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const setLogo = () => {
        const theme = document.documentElement.getAttribute('data-bs-theme');
        const logo = document.getElementById('sidebar-logo');
        if (theme === 'light') {
            logo.src = '<%= context %>/imagenes/logo-color-01.png';
        } else {
            logo.src = '<%= context %>/imagenes/logo-blanco-01.png';
        }
    };

    setLogo();
    document.querySelectorAll('[data-bs-theme-value]').forEach(toggle => {
        toggle.addEventListener('click', () => {
            setTimeout(() => {
                const themeValue = toggle.getAttribute('data-bs-theme-value');
                document.documentElement.setAttribute('data-bs-theme', themeValue);
                setLogo();
            }, 100);
        });
    });

    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', () => {
        setLogo();
    });
});
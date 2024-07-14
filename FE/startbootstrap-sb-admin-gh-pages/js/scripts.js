/*!
    * Start Bootstrap - SB Admin v7.0.7 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2023 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    // 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});


window.addEventListener('DOMContentLoaded', function() {
    const tableBody = document.getElementById('tableBody');
    const rekapDataButton = document.getElementById('rekapDataButton');
    if (!tableBody) {
        return;
    }

    function fetchData() {
        axios.get('http://localhost:8080/inspired/api/list')
            .then(response => {
                const data = response.data.response.list;
                populateTable(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function rekapData() {
        axios.post('http://localhost:8080/inspired/api/rekap', {
            })
            .then(response => {
                console.log(response);
                showSuccessMessage();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function showSuccessMessage() {
        alert('Data berhasil di rekap!');
    }

    function populateTable(data) {
        tableBody.innerHTML = '';

        if (Array.isArray(data)) {
            data.forEach(item => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${item.lob}</td>
                    <td>${item.penyebab_klaim}</td>
                    <td>${item.jumlah_nasabah}</td>
                    <td>${item.beban_klaim}</td>
                `;
                tableBody.appendChild(row);
            });

            const datatablesSimple = document.getElementById('datatablesSimple');
            if (datatablesSimple) {
                new simpleDatatables.DataTable(datatablesSimple);
            }
        } else {
        }
    }

    rekapDataButton.addEventListener('click', function() {
        rekapData();
    });

    fetchData();
});



const hamburger = document.querySelector(".toggle-btn");
const toggler = document.querySelector("#icon");
hamburger.addEventListener("click",function() {
    document.querySelector("#sidebar").classList.toggle("expand");
    toggler.classList.toggle("bx-chevrons-right");
    toggler.classList.toggle("bx-chevrons-left");
});
new Chart(document.getElementById("bar-chart-grouped"), {
    type: 'bar',
    data: {
        labels: ["1900", "1950", "1999", "2050"],
        datasets: [
            {
                label: "Africa",
                backgroundColor: "#3e95cd",
                data: [133,221,783,2478]
            }, {
                label: "Europe",
                backgroundColor: "#8e5ea2",
                data: [408,547,675,734]
            }
        ]
    },
    options: {
        title: {
            display: true,
            text: 'Population growth (millions)'
        }
    }
});
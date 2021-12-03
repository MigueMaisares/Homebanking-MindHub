const app = Vue.createApp({
    data(){
        return {
            cuenta:[],
            transacciones:[],
            clientes:[],
            id:"",
            desde:"",
            hasta:""
        }
    },
    created(){
        const urlSearchParams = new URLSearchParams(window.location.search);
        const params = Object.fromEntries(urlSearchParams.entries());

        this.id = params.id

        axios.get("/api/accounts/" + params.id)
        .then(response => {
            this.cuenta = response.data
            console.log(response.data)
            this.transacciones = response.data.transactions
            this.transacciones.sort((a, b) => a.id < b.id ? 1 : -1)
            console.log(this.transacciones)
        })
        .catch(error => console.log(error.message))

        axios.get("/api/clients/current")
            .then(response => {
                console.log(response)
                this.clientes = response.data
            })
            .catch(error => console.log(error.message))
    },
    methods:{
        logout(){
            axios.post('/api/logout')
                .then(() => window.location.href = "/web/index.html").catch(error => console.log(error.message))
        },
        formatoMoneda(dinero){
            return new Intl.NumberFormat('es-AR', {style: 'currency',currency: 'USD', minimumFractionDigits: 2}).format(dinero);
        },
        formatoFecha(fecha) {
            return new Date(fecha).toLocaleString()
        },
        exportarPDF(){
            if (this.desde && this.hasta){
                axios.post("/api/transactions/export/pdf?id=" + this.id, "&desde=" + this.desde + "T00:00" + "&hasta=" + this.hasta + "T23:59",
                    { headers:{'content-type':'application/x-www-form-urlencoded' }, responseType: 'blob'}
                )
                .then(response => {
                    var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                    var fileLink = document.createElement('a');   
                    fileLink.href = fileURL;
                    fileLink.setAttribute('download', `Statement-Account_${this.cuenta.number}_${new Date().toLocaleString()}.pdf`);
                    document.body.appendChild(fileLink);
                    fileLink.click();
                })
                .catch(error => console.log(error.message))
            } else {
                Swal.fire({
                    title: 'Please enter both dates',
                    showConfirmButton: false,
                    icon: "error",
                    showCloseButton: false,
                    timer: 2000,
                    timerProgressBar: true
                })  
            }
        },
        exportarComprobantePDF(id){
            axios.get("/api/transactions/export/pdf?id=" + id, { responseType: 'blob'})
            .then(response => {
                console.log(response)
                var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                var fileLink = document.createElement('a');   
                fileLink.href = fileURL;
                fileLink.setAttribute('download', `Voucher_${this.cuenta.number}_${new Date().toLocaleString()}.pdf`);
                document.body.appendChild(fileLink);
                fileLink.click();
            })
            .catch(error => console.log(error.message))
        }
    }
})
app.mount("#app")
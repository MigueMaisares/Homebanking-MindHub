const app = Vue.createApp({
    data(){
        return{
            cliente:[],
            cuentas:[],
            transferTo:"",
            accountSource:"",
            accountTarget:"",
            amount:"",
            description:"",
        }
    },
    created(){
        axios.get("/api/clients/current")
        .then(response => {
            console.log(response)
            this.cliente = response.data
            this.cuentas = response.data.accounts.sort((a,b) => a.id>b.id ? 1 : -1)//ASC
        })
        .catch(error => console.error(error.message))
    },
    methods:{
        logout(){
            axios.post('/api/logout').then(() => window.location.href = "/web/index.html").catch(error => console.log(error.message))
        },
        makeTransfer(){
            if (this.amount > 0) {
                Swal.fire({
                    title: 'Are you sure?',
                    text: "You will not be able to reverse the request",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, I wish to apply for the loan'
                })
                .then((result) =>  {
                    if (result.isConfirmed) {
                        axios.post("/api/transactions",
                            "amount=" + this.amount + "&description=" + this.description + "&numberAccOrigin=" + this.accountSource + "&numberAccDestiny=" + this.accountTarget,
                            { headers:{'content-type':'application/x-www-form-urlencoded' }}
                        )
                        .then(
                            Swal.fire({
                                title:'Made',
                                text:'Your transaction will be affected immediately',
                                icon:'success',
                                showConfirmButton: false,
                                timer: 2000,
                                timerProgressBar: true
                            }),
                            setTimeout( () => window.location.href = "/web/accounts.html",2000))
                        .catch(() => 
                            Swal.fire({
                                title: 'Incorrect data',
                                showConfirmButton: false,
                                icon: "error",
                                showCloseButton: false,
                                timer: 2000,
                                timerProgressBar: true
                            })   
                        )
                    }
                })
                } else {
                    Swal.fire({
                        title: 'Please enter a correct amount',
                        showConfirmButton: false,
                        icon: "error",
                        showCloseButton: false,
                        timer: 2000,
                        timerProgressBar: true
                    })   
                }

            
        },
        formatoFecha(fecha) {
            return new Date(fecha).toLocaleString()
        },
        formatoMoneda(dinero){   
            return new Intl.NumberFormat('es-AR', {style: 'currency',currency: 'USD', minimumFractionDigits: 2}).format(dinero);
        }
    },
    computed: {
        cuentasFiltradas(){
            return this.cuentas.filter(cuenta => cuenta.number != this.accountSource)
        },
        cuentaFiltrada(){
            return this.cuentas.filter(cuenta => cuenta.number == this.accountSource)[0].balance
        }
    }
})
app.mount("#app")
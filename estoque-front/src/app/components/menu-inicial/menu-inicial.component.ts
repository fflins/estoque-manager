import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-menu-inicial',
  templateUrl: './menu-inicial.component.html',
  styleUrl: './menu-inicial.component.css',
  standalone: true,
  imports: [RouterModule]
})

export class MenuInicialComponent {

  constructor(private router: Router) {}

  irParaInserir() {
    this.router.navigate(['/inserir-produto']);
  }

  irParaConsultar() {
    this.router.navigate(['/consultar-produtos']);
  }

  irParaRemover() {
    this.router.navigate(['/remover-produto']);
  }

  irParaHistorico() {
    this.router.navigate(['/consultar-movimentacoes']);
  }
}

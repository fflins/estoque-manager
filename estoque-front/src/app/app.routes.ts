import { Routes } from '@angular/router';
import { InserirProdutoComponent } from './components/inserir-produto/inserir-produto.component';
import { ConsultarProdutosComponent } from './components/consultar-produtos/consultar-produtos.component';
import { RemoverProdutoComponent } from './components/remover-produto/remover-produto.component';

export const routes: Routes = [
  { path: 'inserir', component: InserirProdutoComponent },
  { path: 'consultar', component: ConsultarProdutosComponent },
  { path: 'remover', component: RemoverProdutoComponent },
  { path: '', redirectTo: '/inserir', pathMatch: 'full' }
];
